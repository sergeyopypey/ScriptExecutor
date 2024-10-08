# ScriptExecutor
## Использование
1. ### Устанавливаем [IntelliJ IDEA](https://www.jetbrains.com/idea/) (подойдет любое издание).
2. ### Скачиваем последнюю версию [ScriptExecutor.jar](https://github.com/sergeyopypey/ScriptExecutor/releases) и помещаем в удобное место на компьютере
3. ### Заходим в IDEA, переходим File → Settings (Ctrl + Alt + S/)
4. ### Ищем раздел Tools → External Tools
5. ### Нажимаем + и заполняем форму:
   * #### Name: вписываем названием (Run in Jira)
   * #### Group: External Tools
   * #### Description: оставляем пустым
   * #### Program: java
   * #### Arguments: Windows```-jar ScriptExecutor.jar <URL> <TOKEN> $FileDir$\$FileName$```; Mac OS ```-jar ScriptExecutor.jar <URL> <TOKEN> $FileDir$/$FileName$```
   > URL - это REST-endpoint для запуска скриптов. Для Scriptrunner это будет: %HOSTNAME%/rest/scriptrunner/latest/user/exec/<br>
   > TOKEN - это персональный токен доступ, сгенерировать его можно в профиле. [Инструкция](https://confluence.atlassian.com/enterprise/using-personal-access-tokens-1026032365.html)
   * #### Working Directory: здесь указываем путь до [ScriptExecutor.jar](https://github.com/sergeyopypey/ScriptExecutor/releases) из п.2
6. ### Нажимаем Ok → Apply
7. ### Теперь ищем в верхней панели Tools → External Tools → Run in Jira
8. ### Запускаем, в терминале должен появиться результат экзекуции
