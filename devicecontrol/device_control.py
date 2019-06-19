# test BLE Scanning software
# jcs 6/8/2014

import blescan
import sys
#import pygame
#import time
#import pyglet
import vlc
import bluetooth._bluetooth as bluez
import os

dev_id = 0

try:
    sock = bluez.hci_open_dev(dev_id)
    print "ble thread started"

except:
    print "error accessing bluetooth device..."
    sys.exit(1)

blescan.hci_le_set_scan_parameters(sock)
blescan.hci_enable_le_scan(sock)

while True:
    returnedList = blescan.parse_events(sock, 1)
    #print "----------"
    for beacon in returnedList:
        beacon_data = beacon.split(',')
        _uuid = beacon_data[1]
        _major = beacon_data[2]
        _minor = beacon_data[3]
        _tx_power = beacon_data[4]
        if _uuid == '0000123400001000800000805f9b34f1': # level 1
            appName = 'vlc'
            killApp = 'killall -9 ' + appName
            os.system(killApp)
            #appName = 'gpicview'
            #killApp = 'killall -9 ' + appName
            #os.system(killApp)
            warnpic = ('gpicview /home/pi/KNU_SE/lv1.png')
            #warnaud = ('vlc /home/pi/KNU_SE/lv1.mp3')
            os.popen(warnpic)
            p = vlc.MediaPlayer("/home/pi/KNU_SE/lv1.mp3")
            p.play()
#            song = pyglet.media.load('lv1.mp3')
#            song.play()
#            pyglet.app.run()
#            pygame.mixer.init()
#            bang = pygame.mixer.Sound("lv1.mp3")
#            while True:
#                bang.play()
#                time.sleep(2.0)
            #os.popen(warnaud)
        if _uuid == '0000123400001000800000805f9b34f2': # level 2
            appName = 'vlc'
            killApp = 'killall -9 ' + appName
            os.system(killApp)
            appName = 'gpicview'
            killApp = 'killall -9 ' + appName
            os.system(killApp)
            warnpic = ('gpicview /home/pi/KNU_SE/lv2.png')
            warnaud = ('vlc /home/pi/KNU_SE/lv2.mp3')
            os.popen(warnpic)
            #os.popen(warnaud)
        if _uuid == '0000123400001000800000805f9b34f3': # level 3
            appName = 'vlc'
            killApp = 'killall -9 ' + appName
            os.system(killApp)
            appName = 'gpicview'
            killApp = 'killall -9 ' + appName
            os.system(killApp)
            warnpic = ('gpicview /home/pi/KNU_SE/lv3.png')
            warnaud = ('vlc /home/pi/KNU_SE/lv3.mp3')
            os.popen(warnpic)
            #os.popen(warnaud)