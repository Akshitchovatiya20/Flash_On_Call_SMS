import 'package:flutter/material.dart';
import 'package:flutter_sms_listener/flutter_sms_listener.dart';
import 'package:flutter_vibrate/flutter_vibrate.dart';
import 'call/homescreen.dart';

backgrounMessageHandler(SmsMessage message) async {
  Vibrate.vibrate();
}
void main() {
  WidgetsFlutterBinding.ensureInitialized();
  runApp(
    MaterialApp(
      debugShowCheckedModeBanner: false,
      initialRoute: '/',
      routes: {
        '/':(context) => const Hme_Page(),
      },
    ),
  );
}
