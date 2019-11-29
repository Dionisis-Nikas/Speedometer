package unipi.dionisis98.speedometer;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import java.util.Locale;

//Our custom TTS engine class
class TTS {
    private TextToSpeech tts;

    //Implement our listener override method to initialize our TTS object
    private TextToSpeech.OnInitListener initListener =
            new TextToSpeech.OnInitListener() {
                @Override
                public void onInit(int status) {
                    if (status==TextToSpeech.SUCCESS)
                        //if our object is created then set the language to english
                        tts.setLanguage(Locale.ENGLISH);
                }
            };

    TTS(Context context) {
        tts = new TextToSpeech(context,initListener);
    }

    //Our speak method
    void speak(String message){
        tts.speak(message,TextToSpeech.QUEUE_ADD,null,null);
    }

    //Stop and shutdown methods
    void stop(){
        this.stop();
    }
    void shutDown(){ this.shutDown();}
}
