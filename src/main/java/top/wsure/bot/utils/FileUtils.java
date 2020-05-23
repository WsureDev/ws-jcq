package top.wsure.bot.utils;

import com.esotericsoftware.yamlbeans.YamlReader;
import top.wsure.bot.common.exceptions.BotException;
import top.wsure.bot.entity.RobotConfigDo;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;

import static top.wsure.bot.Bot.AppID;
import static top.wsure.bot.Bot.CQ;

/**
 * FileName: FileUtils
 * Author:   wsure
 * Date:     2020/1/20 上午9:06
 * Description:
 */
public class FileUtils {

    public static RobotConfigDo importYaml() throws BotException{
        InputStream in = null;
        CQ.logInfo("yamlPath",CQ.getAppDirectory());
        File dumpFile = new File(CQ.getAppDirectory()+"/setting.yaml");
        try {
            if(dumpFile.exists()){
                in = new FileInputStream(dumpFile);
            } else {
                in = FileUtils.class.getResourceAsStream("../common/config/setting.yaml");
            }
            YamlReader yamlReader = new YamlReader(new InputStreamReader(in));

            RobotConfigDo robotConfigDo = yamlReader.read(RobotConfigDo.class);

            CQ.logInfo("robotConfig", robotConfigDo.toString());
            return robotConfigDo;
        } catch (Exception e) {
            e.printStackTrace();
            throw new BotException("初始化配置文件失败","配置文件不存在");
        }
    }
}
