public class BootFinishReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {

            ArrayList<Alarm> alarmList = new ArrayList<>();
            SharedPreferences alarmDatabase = getSharedPreferences(
                    "AlarmDatabase", Context.MODE_PRIVATE);
            if (alarmDatabase.contains("arraySize")) {
                int toLoadSize = alarmDatabase.getInt("arraySize", 0);
                for (int i = 0; i < toLoadSize; i++) {
                    if (alarmDatabase.getBoolean(i + "on", false)) {
                        Alarm toAdd = new Alarm();
                        toAdd.setMasterID(alarmDatabase.getString(i + "masterID", null));
                        toAdd.setLabel(alarmDatabase.getString(i + "label", null));
                        toAdd.setFromHour(alarmDatabase.getInt(i + "fromHour", 7));
                        toAdd.setFromMinute(alarmDatabase.getInt(i + "fromMinute", 20));
                        toAdd.setToHour(alarmDatabase.getInt(i + "toHour", 7));
                        toAdd.setToMinute(alarmDatabase.getInt(i + "toMinute", 30));
                        toAdd.setNumAlarms(alarmDatabase.getInt(i + "numAlarms", 3));
                        toAdd.setSunday(alarmDatabase.getBoolean(i + "sunday", false));
                        toAdd.setMonday(alarmDatabase.getBoolean(i + "monday", false));
                        toAdd.setTuesday(alarmDatabase.getBoolean(i + "tuesday", false));
                        toAdd.setWednesday(alarmDatabase.getBoolean(i + "wednesday", false));
                        toAdd.setThursday(alarmDatabase.getBoolean(i + "thursday", false));
                        toAdd.setFriday(alarmDatabase.getBoolean(i + "friday", false));
                        toAdd.setSaturday(alarmDatabase.getBoolean(i + "saturday", false));
                        toAdd.setOn(alarmDatabase.getBoolean(i + "on", false));
                        for (int j = 0; j < alarmDatabase.getInt(i + "numAlarms", 3); j++) {
                            toAdd.addAlarmID(alarmDatabase.getInt(i + "alarmID" + j, 0));
                            toAdd.addAlarmName(alarmDatabase.getString(i + "alarmName" + j, null));
                            toAdd.addAlarmUri(alarmDatabase.getString(i + "alarmUri" + j, null));
                            toAdd.addAlarmVibrate(alarmDatabase.getBoolean(i + "alarmVibrate" + j, false));
                        }
                        alarmList.add(toAdd);
                    }
                }
            }
            for (int i = 0; i < alarmList.size(); i++) {
                alarmList.get(i).setAlarm(context, false);
            }
        }
    }
}
