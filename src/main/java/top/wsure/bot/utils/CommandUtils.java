package top.wsure.bot.utils;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import top.wsure.bot.entity.CommandDo;
import top.wsure.bot.entity.MenuDo;
import top.wsure.bot.entity.RobotConfigDo;
import top.wsure.bot.common.enums.ComponentEnum;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static top.wsure.bot.Bot.CQ;
import static top.wsure.bot.common.config.Constants.*;

/**
 * FileName: CommandUtils
 * Author:   wsure
 * Date:     2020/1/17 下午2:31
 * Description:
 */
public class CommandUtils {


    public static List<CommandDo> getCommand(String input){
        String in  = input.replace(getAtMe(),"");

        List<CommandDo> commandList = ROBOT_COMMANDS;
        return commandList.stream()
                .filter( commandDo -> commandDo.getComponentType() != null)
                .filter(
                        commandDo -> filterCommandMap(in, commandDo)
                ).collect(Collectors.toList());
    }

    public static List<MenuDo> getMenu(String input){
        String in  = input.replace(getAtMe(),"");

        List<MenuDo> menus = ROBOT_CONFIG.getMenu();
        return menus.stream()
                .filter( menu -> StringUtils.isNotBlank(menu.getAnswer()) && menu.getQuestion().equals(input))
                .collect(Collectors.toList());
    }

    /**
     *
     * @param robotConfig
     * @return
     */
    public static List<CommandDo> createCommandMap(RobotConfigDo robotConfig){
        List<String> disable = robotConfig.getDisable();
        boolean hasDisable = CollectionUtils.isNotEmpty(disable);
        List<CommandDo> commandListMap = robotConfig.getCommands();
        commandListMap.forEach( value -> {
            value.setComponentType(ComponentEnum.getByType(value.getType()));
            if(hasDisable && (disable.contains(value.getAlia()) || disable.contains(value.getComponentName())))
                value.setEnable(false);
        });
        return commandListMap;
    }

    /**
     * getAtMe
     * @return
     */
    public static String getAtMe(){
        return CC.at(CQ.getLoginQQ());
    }

    /**
     * 识别指令
     * @param input
     * @param cmd
     * @return
     */

    public static boolean filterCommandMap(String input,CommandDo cmd){
        if(!cmd.isEnable()){
            return false;
        }

        String command = cmd.getCommand().matches("[a-zA-Z]+") ?
                ( "(("+cmd.getCommand()+")|("+cmd.getCommand().toUpperCase()+"))" ) :
                cmd.getCommand();

        StringBuilder regex = new StringBuilder("^\\s*" + command + "\\s*");

        Pattern withOutAt;
        withOutAt = Pattern.compile("(?<=" + command + "\\s).+");
        Matcher matcher = withOutAt.matcher(input);

        if (cmd.getComponentType().isNeedParam()) {
            regex.append("\\S+");
        } else {
            regex.append("$");
        }

        if(input.toLowerCase().matches(regex.toString()))
        {
            if (cmd.getComponentType().isNeedParam()) {
                if( matcher.find()){
                    cmd.setParam(matcher.group());
                    return true;
                } else {
                    return false;
                }
            } else {
                return true;
            }
        }
        return false;
    }
}
