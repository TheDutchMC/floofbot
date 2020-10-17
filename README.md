# floofbot
Serve cat pictures to your Discord server

> **Note:** Only files ending in `.jpg` (This must match **exactly**) will be used.

### Installation
1. Download the JAR
2. Donwload Java (8 minimum)
3. Start it once
4. Configure it using floofbot.properties (Generated in the same folder as the JAR)
> Not going to explain this one, there are plenty of tutorials out there who teach you how to set up a Discord bot :)  
  For Windows paths use double `\`
5. Setup whatever your platform uses to run it in the background
   - Windows: Not going to bother with a guide for Windows, since I don't use it for hosting ¯\_(ツ)_/¯
   - Linux (Tested on Ubuntu Focal):
       1. Open ``/etc/systemd/system/floofbot.service`` using your favourite text editor
       2. Paste the following text in it, replace ``/root/`` with wherever you put the JAR:
       ```
       [Unit]
       Description=FloofBot - The Discord Cat Bot

       [Service]
       WorkingDirectory=/root/
       ExecStart=java -jar /root/FloofBot-all.jar

       [Install]
       WantedBy=multi-user.target
       ```
       3. Save
       4. ``systemctl daemon-reload``
       5. ``systemctl enable --now floofbot``
6. Enjoy :)

### Notes
Does this bot have a reason for existing? Nope not really. But who doesn't like cat pictures.

I'm not obligated to keep this bot supported, so it might work, it might not. Thats why it's open source.
