package szregplat.util;

import szregplat.omc.Ws320RuntimeException;
import org.hibernate.HibernateException;
import org.hibernate.usertype.ParameterizedType;
import org.hibernate.usertype.UserType;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.Properties;

/**
 * Created by hch on 2014/7/12.
 */
public class EnumType implements UserType, ParameterizedType {
    private Method getEnumFunc;

    private Method getValueFunc;

    private Class<Enum<?>> enumCls;

    private Object enumObj;


    public void setParameterValues(Properties parameters) {
        if (parameters == null) return;
        String className = parameters.get("enumClass").toString();
        try {
            enumCls = (Class<Enum<?>>) Class.forName(className);
            enumObj = enumCls.getEnumConstants()[0];
            getValueFunc = enumCls.getMethod("getValue");
            getEnumFunc = enumCls.getMethod(
                    "getEnum",
                    new Class[]{String.class}
            );
        } catch (Exception e) {
            throw new Ws320RuntimeException(e);
        }
    }

    public int[] sqlTypes() {
        return new int[]{Types.VARCHAR};
    }

    public Class<?> returnedClass() {
        return enumCls;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        return ObjectUtils.nullSafeEquals(x, y);
    }

    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    public Object nullSafeGet(ResultSet rs, String[] names, Object owner)
            throws HibernateException, SQLException {
        String value = rs.getString(names[0]);
        if (value == null) return null;

        try {
            return getEnumFunc.invoke(enumObj, new Object[]{value});
        } catch (Exception e) {
            throw new Ws320RuntimeException(e);
        }
    }

    /**
     * Fetch the data from enum and set it in prepared statement
     */
    public void nullSafeSet(PreparedStatement st, Object obj, int index)
            throws HibernateException, SQLException {
        if (obj == null) st.setObject(index, null);

        try {
            String val = getValueFunc.invoke(obj).toString();
            st.setString(index, val);
        } catch (Exception e) {
            throw new Ws320RuntimeException(e);
        }
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public boolean isMutable() {
        return false;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        Object deepCopy = deepCopy(value);
        return (Serializable) deepCopy;
    }

    public Object assemble(Serializable cached, Object owner)
            throws HibernateException {
        return deepCopy(cached);
    }

    public Object replace(Object original, Object target, Object owner)
            throws HibernateException {
        return deepCopy(original);
    }
}
