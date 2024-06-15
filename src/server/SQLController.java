package server;

import commonData.data.Coordinates;
import commonData.data.Location;
import commonData.data.Person;
import commonData.data.StudyGroup;
import commonData.data.enums.EyeColor;
import commonData.data.enums.FormOfEducation;
import commonData.data.enums.HairColor;
import commonData.data.enums.Semester;
import commonData.data.interfaces.Element;
import commonData.modelHandlers.Respondent;
import commonData.requests.RequestUser;
import server.model.LinkedListStorage;
import server.model.interfaces.IStore;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Properties;
import java.util.SplittableRandom;

public class SQLController {
    //private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String URL = "jdbc:postgresql://pg/studs";
    private Connection conn;
    private Statement st;
    {
        init();
    }
    public IStore<StudyGroup> getData() throws SQLException {
        ResultSet resultSet = st.executeQuery("select * from studyGroup");
        IStore<StudyGroup> storage = new LinkedListStorage<>();
        while(resultSet.next()){
            StudyGroup studyGroup = new StudyGroup();
            studyGroup.setId(resultSet.getLong("id"));
            studyGroup.setName(resultSet.getString("name"));
            studyGroup.setCreationDate(resultSet.getDate("creationDate").toLocalDate());
            studyGroup.setStudentsCount(resultSet.getInt("studentsCount"));
            studyGroup.setUser(resultSet.getString("user_login"));
            try {
                studyGroup.setFormOfEducation(FormOfEducation.valueOf(resultSet.getString("formOfEducation")));
            } catch (Exception e){

            }
            studyGroup.setSemesterEnum(Semester.valueOf(resultSet.getString("semester")));
            long coordinatesId = resultSet.getLong("coordinatesId");
            var coordinatesSt = conn.prepareStatement("select * from coordinates where id = ?");
            coordinatesSt.setLong(1, coordinatesId);
            ResultSet resCoordinates = coordinatesSt.executeQuery();
            resCoordinates.next();
            Coordinates coordinates = new Coordinates();
            coordinates.setX(resCoordinates.getInt("x"));
            coordinates.setY(resCoordinates.getDouble("y"));
            coordinatesSt.close();
            studyGroup.setCoordinates(coordinates);
            try {
                long personId = resultSet.getLong("groupAdminId");
                var personSt = conn.prepareStatement("select * from person where id = ?");
                personSt.setLong(1, personId);
                ResultSet resPerson = personSt.executeQuery();
                resPerson.next();
                Person person = new Person();
                person.setName(resPerson.getString("name"));
                person.setWeight(resPerson.getFloat("weight"));
                person.setEyeColor(EyeColor.valueOf(resPerson.getString("eyeColor")));
                person.setHairColor(HairColor.valueOf(resPerson.getString("hairColor")));
                long locationId = resPerson.getLong("locationId");
                personSt.close();
                var locationSt = conn.prepareStatement("select * from location where id = ?");
                locationSt.setLong(1, locationId);
                var resLocation = locationSt.executeQuery();
                resLocation.next();
                Location location = new Location();
                location.setName(resLocation.getString("name"));
                location.setX(resLocation.getLong("x"));
                location.setY(resLocation.getInt("y"));
                location.setZ(resLocation.getInt("z"));
                locationSt.close();
                person.setLocation(location);
                studyGroup.setGroupAdmin(person);

            } catch (Exception e){

            }
            storage.addOnly(studyGroup);
        }
        return storage;
    }
    public void init(){
        try {
            Class.forName("org.postgresql.Driver");
            Properties authorization = new Properties();
            try (InputStream input = new FileInputStream("userInfoHelious.properties")){
                authorization.load(input);
            }

            conn = DriverManager.getConnection(URL, authorization);
            st = conn.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

            String query = """
                    create table if not exists userData(
                        login varchar(20) primary key,
                        password varchar(255) not null,
                        salt varchar(100) not null
                    );
                    
                    create table if not exists coordinates (
                    	id bigserial primary key,
                    	x integer check (x <= 724) not null,
                    	y float8 not null
                    );

                    create table if not exists location (
                    	id bigserial primary key,
                    	x int8 not null,
                    	y integer not null,
                    	z integer not null,
                    	name varchar(454)
                    );
                    drop type if exists formOfEducationEnum;
                    create type formOfEducationEnum as enum(
                    	'DISTANCE_EDUCATION',
                    	'FULL_TIME_EDUCATION',
                    	'EVENING_CLASSES'
                    );
                    drop type if exists semesterEnum;
                    create type semesterEnum as enum(
                    	'FIRST',
                    	'FOURTH',
                    	'FIFTH',
                    	'SIXTH',
                    	'SEVENTH'
                    );
                    drop type if exists eyeColorEnum;
                    create type eyeColorEnum as enum(
                    	'GREEN',
                    	'YELLOW',
                    	'WHITE'
                    );
                    drop type if exists hairColorEnum;
                    create type hairColorEnum as enum(
                    	'BLACK',
                    	'YELLOW',
                    	'BROWN'
                    );

                    create table if not exists person(
                    	id bigserial primary key,
                    	name text check(char_length(name) > 0) not null,
                    	weight float check(weight > 0) not null,
                    	eyeColor eyeColorEnum,
                    	hairColor hairColorEnum not null,
                    	locationId serial references location on delete cascade not null
                    );

                    create table if not exists studyGroup(
                    	id bigserial primary key,
                    	name text check(char_length(name) > 0) not null,
                    	coordinatesId integer references coordinates on delete cascade not null,
                    	creationDate date not null,
                    	studentsCount integer check(studentsCount > 0) not null,
                    	formOfEducation formOfEducationEnum,
                    	semester semesterEnum not null,
                    	groupAdminId integer references person on delete set null,
                    	user_login varchar(20) references userData on delete cascade
                    );
                    """;
            //userLogin varchar(255) references user on delete cascade not null
            st.execute(query);
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void add(StudyGroup studyGroup) throws SQLException {
        long coordinatesId = addCoordinates(studyGroup);
        long personId = 0;
        if (studyGroup.getGroupAdmin() != null) {
            long locationId = addLocation(studyGroup);
            personId = addPerson(studyGroup, locationId);
        }
        addStudyGroup(studyGroup, coordinatesId, personId);
    }
    private void addStudyGroup(StudyGroup studyGroup, long coordinatesId, long personId) throws SQLException{
        String query = "insert into studyGroup(name, coordinatesId, studentsCount, formOfEducation, semester, groupAdminId, creationDate, user_login) " +
                "values (?, ?, ?, ?::formOfEducationEnum, ?::semesterEnum, ?, ?, ?)";
        var studyGroupSt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        studyGroupSt.setString(1, studyGroup.getName());
        studyGroupSt.setLong(2, coordinatesId);
        studyGroupSt.setInt(3, studyGroup.getStudentsCount());
        studyGroupSt.setString(8, studyGroup.getUser());
        FormOfEducation formOfEducation = studyGroup.getFormOfEducation();
        if (formOfEducation != null) {
            studyGroupSt.setString(4, String.valueOf(formOfEducation));
        } else {
            studyGroupSt.setNull(4, Types.VARCHAR);
        }
        studyGroupSt.setString(5, String.valueOf(studyGroup.getSemesterEnum()));
        //System.out.println(Date.valueOf(studyGroup.getCreationDate()));
        studyGroupSt.setDate(7, Date.valueOf(studyGroup.getCreationDate()));
        if (studyGroup.getGroupAdmin() == null) {
            studyGroupSt.setNull(6, Types.BIGINT);
            studyGroupSt.executeUpdate();
            ResultSet keys = studyGroupSt.getGeneratedKeys();
            keys.next();
            long studyGroupId = keys.getLong(1);
            studyGroup.setId(studyGroupId);
            studyGroupSt.close();
            return;
        }
        studyGroupSt.setLong(6, personId);
        studyGroupSt.executeUpdate();
        ResultSet keys = studyGroupSt.getGeneratedKeys();
        keys.next();
        long studyGroupId = keys.getLong(1);
        studyGroup.setId(studyGroupId);
        studyGroupSt.close();
    }
    private long addPerson(StudyGroup studyGroup, long locationId) throws SQLException {
        String query = "insert into person (name, weight, eyeColor, hairColor, locationId) " +
                "values (?, ?, ?::eyeColorEnum, ?::hairColorEnum, ?)";
        var personSt = conn.prepareStatement(query);
        personSt.setString(1, studyGroup.getGroupAdmin().getName());
        personSt.setFloat(2, studyGroup.getGroupAdmin().getWeight());
        EyeColor eyeColor = studyGroup.getGroupAdmin().getEyeColor();
        if (eyeColor != null) {
            personSt.setString(3, String.valueOf(eyeColor));
        } else {
            personSt.setNull(3, Types.VARCHAR);
        }
        personSt.setString(4, String.valueOf(studyGroup.getGroupAdmin().getHairColor()));
        personSt.setLong(5, locationId);
        personSt.executeUpdate();
        personSt.close();
        var resultSet = st.executeQuery("select currval('person_id_seq')");
        resultSet.next();
        long res = resultSet.getLong(1);
        personSt.close();
        return res;
    }
    private long addLocation(StudyGroup studyGroup) throws SQLException{
        String query = "insert into location (x, y, z, name) values (?, ?, ?, ?)";
        var locationSt = conn.prepareStatement(query);
        locationSt.setLong(1, studyGroup.getGroupAdmin().getLocation().getX());
        locationSt.setInt(2, studyGroup.getGroupAdmin().getLocation().getY());
        locationSt.setInt(3, studyGroup.getGroupAdmin().getLocation().getZ());
        String personName = studyGroup.getGroupAdmin().getName();
        if (personName != null) {
            locationSt.setString(4, personName);
        } else {
            locationSt.setNull(4, Types.VARCHAR);
        }
        locationSt.executeUpdate();
        locationSt.close();
        var resultSet = st.executeQuery("select currval('location_id_seq')");
        resultSet.next();
        return resultSet.getLong(1);
    }
    private long addCoordinates(StudyGroup studyGroup) throws SQLException{
        String query = "insert into coordinates (x, y) values (?, ?::float8)";
        var coordinatesSt = conn.prepareStatement(query);
        coordinatesSt.setLong(1, studyGroup.getCoordinates().getX());
        coordinatesSt.setString(2, String.valueOf(studyGroup.getCoordinates().getY()));
        coordinatesSt.executeUpdate();
        coordinatesSt.close();
        var resultSet = st.executeQuery("select currval('coordinates_id_seq')");
        resultSet.next();
        return resultSet.getLong(1);
    }
    public void remove(long id) throws SQLException {
        long coordinatesId = getCoordinatesId(id);
        removeObject("coordinates", coordinatesId);
        long personId = getGroupAdminId(id);
        if (personId != 0) {
            removeObject("person", personId);
        }
        removeObject("studyGroup", id);
    }
    private void removeObject(String table, long studyGroupId) throws SQLException {
        var rmCoordinatesSt = conn.prepareStatement("delete from " + table +  " where id = ?");
        rmCoordinatesSt.setLong(1, studyGroupId);
        rmCoordinatesSt.executeUpdate();
        rmCoordinatesSt.close();
    }
    public void clear() throws SQLException{
        st.executeUpdate("TRUNCATE TABLE studyGroup RESTART IDENTITY CASCADE");
        st.executeUpdate("TRUNCATE TABLE location RESTART IDENTITY CASCADE");
        st.executeUpdate("TRUNCATE TABLE coordinates RESTART IDENTITY CASCADE");
        st.executeUpdate("TRUNCATE TABLE person RESTART IDENTITY CASCADE");
    }

    public void update(StudyGroup studyGroup) throws SQLException {
        long coordinatesId = getCoordinatesId(studyGroup.getId());
        long groupAdminId = getGroupAdminId(studyGroup.getId());
        if (groupAdminId == 0 && studyGroup.getGroupAdmin() != null){
            long locationId = addLocation(studyGroup);
            groupAdminId = addPerson(studyGroup, locationId);
        } else if(groupAdminId != 0 && studyGroup.getGroupAdmin() == null){
            removeObject("person", groupAdminId);
        }
        updateCoordinates(studyGroup.getCoordinates(), coordinatesId);
        updateStudyGroup(studyGroup, coordinatesId, groupAdminId);

        if (studyGroup.getGroupAdmin() != null) {
            long locationId = getLocationId(groupAdminId);
            updateLocation(studyGroup.getGroupAdmin().getLocation(), locationId);
            updatePerson(studyGroup.getGroupAdmin(), locationId, groupAdminId);
        }
    }

    private long getCoordinatesId(long studyGroupId) throws SQLException {
        String query = "select coordinatesId from studyGroup where id = ?";
        var getIdSt = conn.prepareStatement(query);
        getIdSt.setLong(1, studyGroupId);
        var resultSt = getIdSt.executeQuery();
        if (resultSt.next()) {
            long res = resultSt.getLong(1);
            getIdSt.close();
            return res;
        } else {
            return 0;
        }
    }

    private long getGroupAdminId(long studyGroupId) throws SQLException {
        String query = "select groupAdminId from studyGroup where id = ?";
        var getIdSt = conn.prepareStatement(query);
        getIdSt.setLong(1, studyGroupId);
        var resultSet = getIdSt.executeQuery();
        if (resultSet.next()) {
            long res = resultSet.getLong(1);
            getIdSt.close();
            return res;
        } else {
            return 0;
        }
    }

    private void updateCoordinates(Coordinates coordinates, long coordinatesId) throws SQLException {
        String query = "UPDATE coordinates SET x = ?, y = ?::float8 WHERE id = ?";
        var coordinatesSt = conn.prepareStatement(query);
        coordinatesSt.setLong(1, coordinates.getX());
        coordinatesSt.setString(2, String.valueOf(coordinates.getY()));
        coordinatesSt.setLong(3, coordinatesId);
        coordinatesSt.executeUpdate();
        coordinatesSt.close();
    }

    private void updateStudyGroup(StudyGroup studyGroup, long coordinatesId, long groupAdminId) throws SQLException {
        String query = "UPDATE studyGroup SET name = ?, coordinatesId = ?, studentsCount = ?, " +
                "formOfEducation = ?::formOfEducationEnum, semester = ?::semesterEnum, " +
                "groupAdminId = ?, creationDate = ? WHERE id = ?";
        var studyGroupSt = conn.prepareStatement(query);
        studyGroupSt.setString(1, studyGroup.getName());
        studyGroupSt.setLong(2, coordinatesId);
        studyGroupSt.setInt(3, studyGroup.getStudentsCount());
        FormOfEducation formOfEducation = studyGroup.getFormOfEducation();
        if (formOfEducation != null) {
            studyGroupSt.setString(4, String.valueOf(formOfEducation));
        } else {
            studyGroupSt.setNull(4, Types.VARCHAR);
        }
        studyGroupSt.setString(5, String.valueOf(studyGroup.getSemesterEnum()));
        studyGroupSt.setDate(7, Date.valueOf(studyGroup.getCreationDate()));

        if (studyGroup.getGroupAdmin() != null) {
            studyGroupSt.setLong(6, groupAdminId);
        } else {
            studyGroupSt.setNull(6, Types.BIGINT);
        }

        studyGroupSt.setLong(8, studyGroup.getId());
        studyGroupSt.executeUpdate();
        studyGroupSt.close();
    }

    private long getLocationId(long groupAdminId) throws SQLException {
        String query = "select locationId from person where id = ?";
        var getLocIdSt = conn.prepareStatement(query);
        getLocIdSt.setLong(1, groupAdminId);
        var resultSet = getLocIdSt.executeQuery();
        if (resultSet.next()) {
            long res = resultSet.getLong(1);
            getLocIdSt.close();
            return res;
        } else {
            throw new SQLException("Location ID not found for group admin ID: " + groupAdminId);
        }
    }

    private void updateLocation(Location location, long locationId) throws SQLException {
        String query = "UPDATE location SET x = ?, y = ?, z = ?, name = ? WHERE id = ?";
        var locationSt = conn.prepareStatement(query);
        locationSt.setLong(1, location.getX());
        locationSt.setInt(2, location.getY());
        locationSt.setInt(3, location.getZ());
        String personName = location.getName();
        if (personName != null) {
            locationSt.setString(4, personName);
        } else {
            locationSt.setNull(4, Types.VARCHAR);
        }
        locationSt.setLong(5, locationId);
        locationSt.executeUpdate();
        locationSt.close();
    }

    private void updatePerson(Person person, long locationId, long groupAdminId) throws SQLException {
        String query = "UPDATE person SET name = ?, weight = ?, eyeColor = ?::eyeColorEnum, " +
                "hairColor = ?::hairColorEnum, locationId = ? WHERE id = ?";
        var personSt = conn.prepareStatement(query);
        personSt.setString(1, person.getName());
        personSt.setFloat(2, person.getWeight());
        EyeColor eyeColor = person.getEyeColor();
        if (eyeColor != null) {
            personSt.setString(3, String.valueOf(eyeColor));
        } else {
            personSt.setNull(3, Types.VARCHAR);
        }
        personSt.setString(4, String.valueOf(person.getHairColor()));
        personSt.setLong(5, locationId);
        personSt.setLong(6, groupAdminId);
        personSt.executeUpdate();
        personSt.close();
    }
    public String getSalt(RequestUser request){
        try {
            String query = "select salt from userData where login = ?";
            var saltSt = conn.prepareStatement(query);
            saltSt.setString(1, request.getLogin());
            ResultSet resultSet = saltSt.executeQuery();
            resultSet.next();
            return resultSet.getString(1);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            //log
            return null;
        }
    }
    public String checkUserInfo(RequestUser request, String salt) {
        try {
            String query = "select password from userData where login = ?";
            var passwordSt = conn.prepareStatement(query);
            passwordSt.setString(1, request.getLogin());
            ResultSet resultSet = passwordSt.executeQuery();
            if (resultSet.next()) {
                String correctPassword = resultSet.getString("password");
                passwordSt.close();
                switch (request.getAuthorizationType()){
                    case LOGIN:
                        return request.getPassword().equals(correctPassword) ? "Success!" : "Wrong password";
                    case CREATE_NEW_ACCOUNT:
                        return "Such login already exists";
                }
            } else {
                switch (request.getAuthorizationType()){
                    case LOGIN: return "No such login";
                    case CREATE_NEW_ACCOUNT:
                        query = "insert into userData (login, password, salt) values (?, ?, ?)";
                        var userSt = conn.prepareStatement(query);
                        userSt.setString(1, request.getLogin());
                        userSt.setString(2, request.getPassword());
                        userSt.setString(3, salt);
                        userSt.executeUpdate();
                        userSt.close();
                        return "Success!";
                }
            }
        } catch(Exception e){
            return e.getMessage();
        }
        return "Try again";
    }
    public void removeUserObjects(String user) throws SQLException {
        String query = "delete from studyGroup where user_login = ?";
        var deleteSt = conn.prepareStatement(query);
        deleteSt.setString(1, user);
        deleteSt.executeUpdate();
        deleteSt.close();
    }
}
