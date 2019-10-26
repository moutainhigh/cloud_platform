package cn.sensordb2.stcloud.otherTools;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * CsvParser
 */
public class CsvParser{
    // Saved input CSV file pathname
    private String inputCsvFile;

    // Space mark , ; : etc.
    private String spaceMark=",";

    /**
     * Contructor
     * @param inputCsvFile
     */
    public CsvParser(String inputCsvFile,String spaceMark){
        this.inputCsvFile=inputCsvFile;
        this.spaceMark=spaceMark;
    }

    /**
     * Contructor
     * @param inputCsvFile
     */
    public CsvParser(String inputCsvFile){
        this.inputCsvFile=inputCsvFile;
        this.spaceMark=",";
    }

    /**
     * Get parsed array from CSV file
     * @return
     */
    public Object[] getParsedArray() throws Exception{
        List<List<String>> retval=new ArrayList<List<String>>();

        String regExp = getRegExp();
        BufferedReader in = new BufferedReader(new FileReader(this.inputCsvFile));
        String strLine;
        String str = "";

        while ((strLine = in.readLine()) != null) {
            Pattern pattern = Pattern.compile(regExp);
            Matcher matcher = pattern.matcher(strLine);
            List<String> listTemp = new ArrayList<String>();
            while (matcher.find())
            {
                str = matcher.group();
                str = str.trim();

                if (str.endsWith(spaceMark))
                {
                    str = str.substring(0, str.length() - 1);
                    str = str.trim();
                }

                if (str.startsWith("\"") && str.endsWith("\""))
                {
                    str = str.substring(1, str.length() - 1);
                    if (CsvParser.isExisted("\"\"", str))
                    {
                        str = str.replaceAll("\"\"", "\"");
                    }
                }

                if (!"".equals(str))
                {
                    listTemp.add(str);
                }
            }

            // Add to retval
            retval.add(listTemp);
        }
        in.close();

        return retval.toArray();
    }

    /**
     * Regular Expression for CSV parse
     * @return
     */
    private String getRegExp()
    {
        final String SPECIAL_CHAR_A = "[^\",\\n 　]";
        final String SPECIAL_CHAR_B = "[^\""+spaceMark+"\\n]";

        StringBuffer strRegExps = new StringBuffer();
        strRegExps.append("\"((");
        strRegExps.append(SPECIAL_CHAR_A);
        strRegExps.append("*["+spaceMark+"\\n 　])*(");
        strRegExps.append(SPECIAL_CHAR_A);
        strRegExps.append("*\"{2})*)*");
        strRegExps.append(SPECIAL_CHAR_A);
        strRegExps.append("*\"[ 　]*"+spaceMark+"[ 　]*");
        strRegExps.append("|");
        strRegExps.append(SPECIAL_CHAR_B);
        strRegExps.append("*[ 　]*"+spaceMark+"[ 　]*");
        strRegExps.append("|\"((");
        strRegExps.append(SPECIAL_CHAR_A);
        strRegExps.append("*["+spaceMark+"\\n 　])*(");
        strRegExps.append(SPECIAL_CHAR_A);
        strRegExps.append("*\"{2})*)*");
        strRegExps.append(SPECIAL_CHAR_A);
        strRegExps.append("*\"[ 　]*");
        strRegExps.append("|");
        strRegExps.append(SPECIAL_CHAR_B);
        strRegExps.append("*[ 　]*");
        return strRegExps.toString();
    }

    /**
     * If argChar is exist in argStr
     * @param argChar
     * @param argStr
     * @return
     */
    private static boolean isExisted(String argChar, String argStr)
    {

        boolean blnReturnValue = false;
        if ((argStr.indexOf(argChar) >= 0)
                && (argStr.indexOf(argChar) <= argStr.length()))
        {
            blnReturnValue = true;
        }
        return blnReturnValue;
    }

    /**
     * Test
     * @param args
     * @throws Exception
     */
    public static void main(String[] args)  throws Exception{
        CsvParser parser=new CsvParser("/Users/sensordb/liangliu/hycloud/maven/hycloud/tmp/accounts.csv");
        //CsvParser parser=new CsvParser("C:\\Users\\IBM_ADMIN\\Desktop\\Test CSV Files\\dummydata_2.csv",":");

        Object[] arr=parser.getParsedArray();
        //System.out.println(arr);

        for(Object obj:arr){
            System.out.print("[");

            List<String> ls=(List<String>)obj;

            for(String item:ls){
                System.out.println(item+",");
            }

            System.out.println("],");
        }
    }
}