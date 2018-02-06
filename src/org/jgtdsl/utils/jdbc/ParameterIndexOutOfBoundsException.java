
/**
 * Title:        <p>
 * Description:  <p>
 * Copyright:    Copyright (c) Troy Thompson, Bob Byron<p>
 * Company:      JavaUnderground<p>
 * @author       Troy Thompson, Bob Byron
 * @version 1.1
 */
package org.jgtdsl.utils.jdbc;

import java.sql.SQLException;

public class ParameterIndexOutOfBoundsException extends SQLException {

    public ParameterIndexOutOfBoundsException() {
        super();
    }

    public ParameterIndexOutOfBoundsException(String s) {
        super(s);
    }
}