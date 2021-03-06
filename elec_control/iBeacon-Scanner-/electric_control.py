# test BLE Scanning software
# jcs 6/8/2014

import threading
import blescan
import sys
import RPi.GPIO as GPIO
import time

import bluetooth._bluetooth as bluez

GPIO.setmode(GPIO.BCM)
GPIO.setup(21, GPIO.OUT)

dev_id = 0
lock = threading.Lock()
try:
    sock = bluez.hci_open_dev(dev_id)
    print("ble thread started")

except:
    print("error accessing bluetooth device...")
    sys.exit(1)

blescan.hci_le_set_scan_parameters(sock)
blescan.hci_enable_le_scan(sock)
global returnedList
global uuidList
returnedList=[]
uuidList=[]


def scan():
	print('scan()')
	while True:
		List = blescan.parse_events(sock, 5)
		print('List: {}'.format(List))
		for beacon in List:
			beacon_data = beacon.split(',')
			_uuid = beacon_data[1]
			_major = beacon_data[2]
			_minor = beacon_data[3]
			_tx_power = beacon_data[4]
			
			print('uuid: {}'.format(_uuid))
			if _uuid in uuidList:
				continue
			else:
				returnedList.append(beacon)
				uuidList.append(_uuid)
			# if _uuid == '08ffffff000102030408163412030102':
			if _uuid == '012f4bf49e724fbe8d5fdcfb07954260':
			# if _uuid == '0000992500001000800000805F9B34FB':
				del uuidList[:]
				lock.acquire()


def test():
	while True:
		mg = input('y, n, b >> ')
		if mg == 'y':
			print('on')
			GPIO.output(21, False)
			continue
		elif mg == 'n':
			print('off')
			GPIO.output(21, True)
			continue
		elif mg == 'b':
			return
		else:
			sys.exit()


if __name__=='__main__':
	test()

	sc = threading.Thread(target = scan, args=())
	sc.start()
	while True:
		# print('start', end=' ')
		GPIO.output(21, True)
		for beacon in returnedList:
			beacon_data = beacon.split(',')
			_uuid = beacon_data[1]
			_major = beacon_data[2]
			_minor = beacon_data[3]
			_tx_power = beacon_data[4]
			print(beacon)
			print('uuid: {}'.format(_uuid))
			# if _uuid == '08ffffff000102030408163412030102':
			if _uuid == '012f4bf49e724fbe8d5fdcfb07954260':
			# if _uuid == '0000992500001000800000805F9B34FB':
				print('on')
				GPIO.output(21, False)
				time.sleep(5)
				print('off')
				GPIO.output(21,True)
				time.sleep(5)
				del returnedList[:]
				lock.release()
				break
