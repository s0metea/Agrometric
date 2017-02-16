#*Agrometric*
Мониторинг и аналитика для умных фермеров

(Для тебя)

##Запуск jar файла:
`java -jar Agrometric.jar`

##Включение Bluetooth:

`rfkill unblock bluetooth`

`bluetoothctl`

`agent DisplayYesNo`

`default-agent`

Устанавливаем свойства для Эдисона:

`discoverable yes`

`pairable on`

`trust XX:XX:XX:XX:XX:XX`

Включить геймпад. Если геймпад подключается к данному Эдисону впервые, необходимо проводное соединение:

`scan on`

`pair XX:XX:XX:XX:XX:XX`

В случае ошибки отключить USB и включить геймпад

PIN code: `root`

`connect XX:XX:XX:XX:XX:XX`

Если требуется предоставить авторизацию услугам — `yes`

Войдя в Dualshock4 устанавливаем соединение с Эдисоном:

`pair XX:XX:XX:XX:XX:XX`

`connect XX:XX:XX:XX:XX:XX`

`info`

`quit`

В случае верной настройки при перезапуске платы достаточно ввести одну команду 
на разблокировку блютуса и включить геймпад. 
После чего появится событие event2 и все будет работать.

Написание скрипта:

`sudo nano /etc/init.d/local.autostart`

В файле прописываем:

`#!/bin/sh`

`rfkill unblock bluetooth`

Теперь необходимо проставить скрипту права на запуск:

`sudo chmod +x /etc/init.d/local.autostart`
`

И наконец, сконфигурировать систему, чтобы наш скрипт запускался при загрузке ОС:

`sudo update-rc.d local.autostart defaults 80`

Документация: http://www.intel.com/content/dam/support/us/en/documents/edison/sb/edisonbluetooth_331704007.pdf