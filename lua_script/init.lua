--author: Zhuoyi Li
-- lua script that will run automatically after the wifi module reboot
-- The script connects the wifi dongle to an access point as well as sets its DNS server


--connect to Access Point 
wifi.setmode(wifi.STATION)
station_cfg={}
--station_cfg.ssid="TELUS5905"
--station_cfg.pwd="7xtjr8njb3"
station_cfg.ssid="SHAW-FDDD90"
station_cfg.pwd="25115B046334"
station_cfg.save=true --set to true means save config to flash
wifi.sta.config(station_cfg)
wifi.sta.autoconnect(1)

tmr.delay(2000000) --wait 2,000,000 us = 2 second

--set up DNS server for the wifi dongle
net.dns.setdnsserver("8.8.8.8", 0) --the primary DNS server the wifi dongle is gonning to use
net.dns.setdnsserver("192.168.1.254", 1) --the secondary DNS server


--print out connection status and ip to check if successfully connected.
print(wifi.sta.status())
print(wifi.sta.getip())
