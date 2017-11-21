package gorm.tools

import grails.util.Holders
import groovy.transform.CompileDynamic
import groovy.transform.CompileStatic
import org.springframework.jdbc.core.JdbcTemplate

import java.sql.SQLException

@SuppressWarnings(['NonFinalPublicField'])
@CompileStatic
class DbDialectService {

    private static final int UNKNOWN = 0
    private static final int MSSQL   = 1
    private static final int MYSQL   = 2
    private static final int ORACLE  = 3

    //GrailsApplication grailsApplication
    JdbcTemplate jdbcTemplate

    static String dialectName

    //need this static so that getGlobalVariables can be accessed from doWithSpring in rally plugin
    @CompileDynamic
    private static int setupDialect() {
        int result = UNKNOWN
        if(!dialectName) dialectName = Holders.grailsApplication.config.hibernate.dialect	// just to make the stuff below easier to read.
        if(dialectName.contains("SQLServerDialect"))         result = MSSQL
        else if(dialectName.contains("MySQL5InnoDBDialect")) result = MYSQL
        else if(dialectName.contains("Oracle")) 			 result = ORACLE
        if(result == UNKNOWN) throw new SQLException("Unknown dialect ${dialectName} in nine.rally.DbDialectService.\n"
                + "Please use a known dialect or make accommodation for a new dialect.")
        return result
    }

    int getDialect() {
        return setupDialect()
    }

    String getCurrentDate() {
        String date
        switch (dialect) {
            case MSSQL: date = "getdate()"; break
            case MYSQL: date = "now()"; break
            case ORACLE: date = "SYSDATE"; break
            default: date = "now()"
        }
        date
    }

    String getIfNull() {
        String ifnull
        switch (dialect) {
            case MSSQL: ifnull = "isnull"; break
            case MYSQL: ifnull = "ifnull"; break
            case ORACLE: ifnull = "NVL"; break
            default: ifnull = "ifnull"
        }
        ifnull
    }

    //concatenation operater
    String getConcat() {
        String concat
        switch (dialect) {
            case MSSQL: concat = "+"; break
            case MYSQL: concat = "+"; break
            case ORACLE: concat = "||"; break
            default: concat = "+"
        }
        concat
    }
    //CHAR/CHR Function
    String getCharFn() {
        String charFn
        switch (dialect) {
            case MSSQL: charFn = "CHAR"; break
            case MYSQL: charFn = "CHAR"; break
            case ORACLE: charFn = "CHR"; break
            default: charFn = "CHAR"
        }
        charFn
    }

    //SUBSTRING Function
    String getSubstringFn() {
        String substringFn
        switch (dialect) {
            case MSSQL: substringFn = "SUBSTRING"; break
            case MYSQL: substringFn = "SUBSTRING"; break
            case ORACLE: substringFn = "SUBSTR"; break
            default: substringFn = "SUBSTRING"
        }
        substringFn
    }

    String getDialectName() {
        String dialectName
        switch (dialect) {
            case MSSQL: dialectName = "dialect_mssql"; break
            case MYSQL: dialectName = "dialect_mysql"; break
            case ORACLE: dialectName = "dialect_oracle"; break
            default: dialectName = "dialect_mysql"
        }
        dialectName
    }

    String getTop(num) {
        String top
        switch (dialect) {
            case MSSQL: top = "TOP ${num}"; break
            case MYSQL: top = "LIMIT ${num}"; break
            case ORACLE: top = "ROWNUM <=${num}"; break
            default: top = "LIMIT ${num}"
        }
        top
    }

    void updateOrDateFormat() {
        if (dialect == ORACLE) {
            String alterOrDateFormat = "alter session set nls_date_format = 'YYYY-MM-dd hh24:mi:ss'"

            jdbcTemplate.update(alterOrDateFormat)
        }
    }

    /** hack for Oracle date formats **/
    @CompileDynamic
    String getDateFormatForDialect(myDate) {
        if (getDialect() == ORACLE) {
            Date dateobj
            if (myDate instanceof String) {
                dateobj = new Date(myDate)
            }
            else {
                dateobj = myDate
            }
            String formattedDate = dateobj.format("yyyy-MM-dd hh:mm:ss")
            return " to_date (\' $formattedDate \', \'YYYY-MM-dd hh24:mi:ss\')"
        }
        //do nothing for all the others
        return myDate
    }

    static Map getGlobalVariables() {
        Map result = [:]
        int dialect = setupDialect()
        if (dialect == MYSQL) {
            result.concat = "FN9_CONCAT"
        } else if (dialect == MSSQL) {
            result.concat = "dbo.FN9_CONCAT"
        } else if (dialect == ORACLE) {
            result.concat = "FN9_CONCAT"
        }

        return result
    }

    boolean isMySql() {
        return dialect == MYSQL
    }

    boolean isMsSql() {
        return dialect == MSSQL
    }

}
