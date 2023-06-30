import 'dart:async';
import 'dart:io';
import 'package:flashlight/flashlight.dart';
import 'package:flutter/material.dart';
import 'package:flutter_sms_listener/flutter_sms_listener.dart';
import 'package:flutter_vibrate/flutter_vibrate.dart';
import 'package:pdf_reader/utils/size.dart';
import 'package:permission_handler/permission_handler.dart';
import 'package:phone_state/phone_state.dart';
import 'package:shared_preferences/shared_preferences.dart';

class Hme_Page extends StatefulWidget {
  const Hme_Page({Key? key}) : super(key: key);

  @override
  State<Hme_Page> createState() => _Hme_PageState();
}

class _Hme_PageState extends State<Hme_Page> {
  bool call_switch = false;
  bool sms_switch = false;
  bool click = false;
  late SharedPreferences prefs;
  bool flash = false;
  bool isflashing = false;
  FlutterSmsListener smsListener = FlutterSmsListener();

  @override
  void initState() {
    super.initState();
    if (!Platform.isAndroid) {
      return;
    }
    WidgetsBinding.instance.addPostFrameCallback((timeStamp) {
      call_light();
      sms_flash();
    });
    getdata();
    permission();
  }

  Future<void> getdata()
  async{
    prefs = await SharedPreferences.getInstance();
    setState(() {
      call_switch = prefs.getBool("call") ?? false;
      sms_switch = prefs.getBool("sms") ?? false;
    });
  }
  void permission()
  async{
    var call = await Permission.phone.request();
    var sms = await Permission.sms.request();
    if(call.isGranted && sms.isGranted)
      {
      }
  }

  void call_light()
  async{
    PhoneState.phoneStateStream.listen((PhoneStateStatus? event) {
      if(call_switch && event == PhoneStateStatus.CALL_INCOMING)
        {
          callFlash();
          print("==================call is coming========");
        }
      else if(event == PhoneStateStatus.CALL_ENDED|| event == PhoneStateStatus.CALL_STARTED)
        {
          stop();
          print("==================call is ended========");
        }
    });
  }

  void sms_flash()
  async{
    smsListener.onSmsReceived!.listen((SmsMessage event) {
       if(sms_switch)
       {
         turnon();
         Future.delayed(const Duration(milliseconds: 800));
         turnoff();
       }
    });
  }

  void callFlash()
  {
    isflashing = true;
    while(isflashing)
    {
      toogle();
      Future.delayed(const Duration(milliseconds: 600));
    }
  }

  void stop()
  {
    if(isflashing)
      {
        turnoff();
        isflashing = false;
        return;
      }
  }
  void turnon()
  {
    Flashlight.lightOn();
  }
  void turnoff()
  {
    Flashlight.lightOff();
  }
  void toogle()
  {
    if(flash)
    {
      turnoff();
    }else{
      turnon();
    }
    flash =! flash;
  }


  @override
  void dispose() {
    super.dispose();
  }
  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        elevation: 0,
        centerTitle: true,
        title: const Text("Flash On Call & SMS"),
        backgroundColor: Colors.green.shade700,
      ),
      body: Column(
        children: [
          Container(
            margin: const EdgeInsets.all(12),
            decoration: BoxDecoration(
              color: Colors.white,
                borderRadius: BorderRadius.circular(12),
              boxShadow: const [
                BoxShadow(
                  color: Colors.grey,
                  blurRadius: 4,
                  offset: Offset(0,1)
                ),
              ]
            ),
            child: Column(
              children: [
                ListTile(
                  title: Text("Incoming Call",
                    style: TextStyle(
                      color: Colors.green.shade700,
                      fontSize: 19,
                    ),
                  ),
                  trailing: Switch(
                      value: call_switch,
                      activeColor: Colors.green.shade700,
                      onChanged: (value)
                      async{
                        setState(() {
                          call_switch = value;
                        });
                        if(call_switch)
                        {
                          call_light();
                        }
                        else{
                          Flashlight.lightOff();
                        }
                        await prefs.setBool("call", call_switch);
                      }
                  ),
                ),
                Divider(
                  color: Colors.grey.shade500,
                ),
                ListTile(
                  title: Text("Incoming SMS",
                    style: TextStyle(
                      color: Colors.blue.shade700,
                      fontSize: 19,
                    ),
                  ),
                  trailing: Switch(
                      value: sms_switch,
                      activeColor: Colors.blue.shade700,
                      onChanged: (value)
                      async{
                        setState(() {
                          sms_switch = value;
                        });
                        if(sms_switch)
                        {
                          sms_flash();
                        }
                        else{
                          Flashlight.lightOff();
                        }
                        await prefs.setBool("sms", sms_switch);
                      }
                  ),
                ),
              ],
            ),
          ),
          H(30),
          GestureDetector(
            onTap: (){
              setState(() {
                click =! click;
              });
              if(click)
                {
                  Vibrate.vibrate();
                  turnon();
                }
              else{
                turnoff();
              }
            },
            child: Container(
              alignment: Alignment.center,
              height: 150,
              width: 150,
              decoration: BoxDecoration(
                shape: BoxShape.circle,
                color: click ? Colors.green.shade700 : Colors.black,
                border: Border.all(
                  color: Colors.black,
                  width: 7
                ),
              ),
              child: Container(
                height: 135,
                width: 135,
                decoration: BoxDecoration(
                  shape: BoxShape.circle,
                  color: click ? Colors.green.shade700 : Colors.grey,
                ),
              ),
            ),
          ),
        ],
      ),
    );
  }
}
