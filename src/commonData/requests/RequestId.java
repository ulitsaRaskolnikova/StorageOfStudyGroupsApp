package commonData.requests;

import commonData.commandData.CommandType;
import commonData.requests.interfaces.IRequestId;

public class RequestId extends RequestOnlyCommand implements IRequestId {
    private long id;

    public RequestId(CommandType commandType, long id) {
        super(commandType);
        this.id = id;
    }

    @Override
    public void setId(long id) {
        this.id = id;
    }

    @Override
    public long getId() {
        return id;
    }
    @Override
    public String toString(){
        return super.toString() +
                "[id=" + id + "]";
    }
}
