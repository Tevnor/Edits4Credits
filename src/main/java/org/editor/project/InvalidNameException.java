package org.editor.project;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class InvalidNameException extends Exception{
    private static final Logger IVE_LOGGER = LogManager.getLogger(InvalidNameException.class);

    public InvalidNameException() {
        super();
        IVE_LOGGER.warn("InvalidNameException thrown");
    }
}
