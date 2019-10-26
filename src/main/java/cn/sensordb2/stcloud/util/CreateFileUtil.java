package cn.sensordb2.stcloud.util;

import java.io.File;
import java.io.IOException;

public class CreateFileUtil {

    public static boolean createFile(String destFileName) {
        File file = new File(destFileName);
        if(file.exists()) {
            return false;
        }
        if (destFileName.endsWith(File.separator)) {
            return false;
        }
        //�ж�Ŀ���ļ����ڵ�Ŀ¼�Ƿ����
        if(!file.getParentFile().exists()) {
            //���Ŀ���ļ����ڵ�Ŀ¼�����ڣ��򴴽���Ŀ¼
            if(!file.getParentFile().mkdirs()) {
                return false;
            }
        }
        //����Ŀ���ļ�
        try {
            if (file.createNewFile()) {
                return true;
            } else {
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean createDir(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            return false;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //����Ŀ¼
        if (dir.mkdirs()) {
            return true;
        } else {
            return false;
        }
    }


    public static String createTempFile(String prefix, String suffix, String dirName) {
        File tempFile = null;
        if (dirName == null) {
            try{
                //��Ĭ���ļ����´�����ʱ�ļ�
                tempFile = File.createTempFile(prefix, suffix);
                //������ʱ�ļ���·��
                return tempFile.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("������ʱ�ļ�ʧ�ܣ�" + e.getMessage());
                return null;
            }
        } else {
            File dir = new File(dirName);
            //�����ʱ�ļ�����Ŀ¼�����ڣ����ȴ���
            if (!dir.exists()) {
                if (!CreateFileUtil.createDir(dirName)) {
                    System.out.println("������ʱ�ļ�ʧ�ܣ����ܴ�����ʱ�ļ����ڵ�Ŀ¼��");
                    return null;
                }
            }
            try {
                //��ָ��Ŀ¼�´�����ʱ�ļ�
                tempFile = File.createTempFile(prefix, suffix, dir);
                return tempFile.getCanonicalPath();
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println("������ʱ�ļ�ʧ�ܣ�" + e.getMessage());
                return null;
            }
        }
    }

    public static void main(String[] args) {
        //����Ŀ¼
        String dirName = "D:/work/temp/temp0/temp1";
        CreateFileUtil.createDir(dirName);
        //�����ļ�
        String fileName = dirName + "/temp2/tempFile.txt";
        CreateFileUtil.createFile(fileName);
        //������ʱ�ļ�
        String prefix = "temp";
        String suffix = ".txt";
        for (int i = 0; i < 10; i++) {
            System.out.println("��������ʱ�ļ���"
                    + CreateFileUtil.createTempFile(prefix, suffix, dirName));
        }
        //��Ĭ��Ŀ¼�´�����ʱ�ļ�
        for (int i = 0; i < 10; i++) {
            System.out.println("��Ĭ��Ŀ¼�´�������ʱ�ļ���"
                    + CreateFileUtil.createTempFile(prefix, suffix, null));
        }
    }

}