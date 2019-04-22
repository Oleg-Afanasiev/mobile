FOR /F "tokens=5 delims= " %%P IN ('netstat -a -n -o ^| findstr ]:8081') DO  TaskKill.exe /F /PID %%P


