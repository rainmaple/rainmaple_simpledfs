package cn.edu.ruc.adcourse.fileClient;


import cn.edu.ruc.adcourse.exceptions.EncryptFailException;
import cn.edu.ruc.adcourse.info.APIInfo;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.Scanner;

/**
 * 客户端程序，负责对象的创建，以及提供服务
 * Created by Rainmaple on 2019/10/23 0006.
 */
public class FileClient_bootstrap {
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, EncryptFailException {
        System.out.println("请输入文件相关操作命令，例如：upload path/file,download [],remove []!");
        while (true){
            Scanner scanner = new Scanner(System.in);
            String input = scanner.nextLine();
            if(input.equals("quit"))
                break;
            args = input.split(" ");
            if (args.length != 2)
                System.out.println("请输入正确的参数例如：upload [],download [],remove []!");
            //从配置文件中初始化
            APIInfo.init();
            FileTransOperator fco = new FileTransSupport(APIInfo.ip, APIInfo.port);
            switch (args[0].trim().toLowerCase()) {
                case "upload":
                    fco.upload(args[1].trim());
                    break;
                case "download":
                    fco.download(args[1].trim());
                    break;
                case "remove":
                    fco.remove(args[1].trim());
            }
        }
    }
}
