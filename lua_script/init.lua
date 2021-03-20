--connect to Access Point (DO NOT save config to flash)
station_cfg={}
station_cfg.ssid="TELUS5905"
station_cfg.pwd="7xtjr8njb3"
station_cfg.save=false
wifi.sta.config(station_cfg)
wifi.sta.autoconnect(1)

tmr.delay(1000000) --wait 2,000,000 us = 2 second
net.dns.setdnsserver("8.8.8.8", 0)
net.dns.setdnsserver("192.168.1.254", 1)
print(wifi.sta.status())
print(wifi.sta.getip())
