package fileSystem;

import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXParseException;

/**
 * NullErrorHandler was created to hide fatal error messages.
 */
class NullErrorHandler implements ErrorHandler {
    @Override
    public void fatalError(SAXParseException e) {
    }

    @Override
    public void error(SAXParseException e) {
    }

    @Override
    public void warning(SAXParseException e) {
    }
}
