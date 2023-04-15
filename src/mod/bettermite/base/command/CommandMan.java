package mod.bettermite.base.command;

import mod.bettermite.base.annotation.SubscribeCommand;
import mod.bettermite.base.interfaces.IBetterMITECommand;
import mod.bettermite.base.interfaces.ICommandArguments;

import net.minecraft.ChatMessage;
import net.minecraft.ServerPlayer;

import java.util.ArrayList;
import java.util.List;

@SubscribeCommand(command = "man")
public class CommandMan implements IBetterMITECommand {

    public static List<ManDocument> manDocumentList = new ArrayList<>();

    @Override
    public void executeCommand(ICommandArguments arg) {
        String string = arg.getCommand();
        ServerPlayer serverPlayer = arg.getServerPlayer();
        if (string.length() == 3 || string.length() == 4) {
            for (int i = 0; i < manDocumentList.size(); i++) {
                serverPlayer.sendChatToPlayer(ChatMessage.createFromText("可用文档" + manDocumentList.get(i).name + "的id为" + manDocumentList.get(i).id));
                return;
            }
        } else {
            string = string.substring(4);
            Integer index = 0;
            String name = new String();
            Object[] args = this.analyzeCommand(serverPlayer, string, index, name);
            try {
                index = (int) args[0];
                name = (String) args[1];
            } catch (ArrayIndexOutOfBoundsException | NullPointerException e) {
                serverPlayer.sendChatToPlayer(ChatMessage.createFromText("你要看什么文档？"));
                throw new RuntimeException("执行/man时出现错误", e);
            }
            if (index < 0) {
                serverPlayer.sendChatToPlayer(ChatMessage.createFromText("页码不能为负数"));
                return;
            }
            for (int i = 0; i < manDocumentList.size(); i++) {
                if (manDocumentList.get(i).name().equals(name)) {
                    if (manDocumentList.get(i).pages().size() < index) {
                        serverPlayer.sendChatToPlayer(ChatMessage.createFromText("你输入的页码过大！最大值为" + manDocumentList.get(i).pages().size()));
                        return;
                    } else {
                        serverPlayer.sendChatToPlayer(ChatMessage.createFromText("您正在阅读" + manDocumentList.get(i).name() + "的第" + index + "页"));
                        ManDocument.ManPage pages = manDocumentList.get(i).pages().get(index);
                        for (int i_ = 0; i_ < pages.length; i++) {
                            serverPlayer.sendChatToPlayer(ChatMessage.createFromText(pages.document.get(i)));
                        }
                        return;
                    }
                } else {
                    serverPlayer.sendChatToPlayer(ChatMessage.createFromText("找不到文档 " + name));
                    return;
                }
            }
        }
    }

    public static class ManDocument {

        private String name;
        private List<ManPage> pages;
        private Integer id;

        public ManDocument(String name, List pages, int id) {
            this.name = name;
            this.pages = pages;
            this.id = id;
        }

        public String name() {
            return name;
        }

        public List<ManPage> pages() {
            return pages;
        }

        public Integer id() {
            return id;
        }

        public static class ManPage {
            public Integer length;
            public List<String> document;

            public ManPage(List<String> document) {
                this.length = document.size();
                this.document = document;
            }
        }

    }

}
