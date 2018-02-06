package org.jgtdsl.utils.jdbc;

/**
 * Title:
 * Description:
 * Copyright:    Copyright (c)
 * Company:
 * @author
 * @version 1.1
 */

public class DebugLevel {

    //private constructor keeps all instances within class
    private DebugLevel(){
    }

    //only allowed values for debugging
    /**
     * Turn debugging off
     */
    public static DebugLevel OFF     = new DebugLevel();

    /**
     * Turn debugging on
     */
    public static DebugLevel ON      = new DebugLevel();

    /**
     * Set debugging to verbose
     */
    public static DebugLevel VERBOSE = new DebugLevel();


}