/**
 * file: SpeechToSpeechTranslatorApp.java
 * author: Bradley Lamitie
 * course: MSCS 621
 *
 * This file contains the declaration of the SpeechToSpeechTranslatorApp class
 */

package application;

import com.ibm.watson.developer_cloud.http.HttpMediaType;
import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.RecognizeOptions;
import com.ibm.watson.developer_cloud.speech_to_text.v1.model.SpeechRecognitionResults;
import com.ibm.watson.developer_cloud.speech_to_text.v1.websocket.BaseRecognizeCallback;
import javafx.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.sound.sampled.*;
import java.io.IOException;
import java.util.ArrayList;

@Controller
public class TranslatorController {
    private static TranslationInfo translationInfo = new TranslationInfo();
    static String languageTo;
    static String languageFrom;
    static String voiceTo;
    private static String voiceFrom = "en-US_NarrowbandModel";
    static String languageFromOptions;
    static String languageToOptions;
    static String voiceToOptions;
    static ArrayList<Integer> chosenIndex = new ArrayList<>();
    static int sampleRate = 16000;
    static AudioFormat format = new AudioFormat(sampleRate, 16, 1, true, false);
    static DataLine.Info info = new DataLine.Info(TargetDataLine.class, format);

    static TargetDataLine line;

    static {
        try {
            line = (TargetDataLine) AudioSystem.getLine(info);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private static final int SECONDS = 10;
    public static void start() throws LineUnavailableException, InterruptedException {
        if (!AudioSystem.isLineSupported(info)) {
            System.out.println("Line not supported");
            System.exit(0);
        }
        line.open(format);

        voiceFrom = defineVoiceModelFrom(languageFrom);
        getTranscripts(line);
    }


    private static void getTranscripts(TargetDataLine line) throws LineUnavailableException, InterruptedException {
        IamOptions iamOptions = new IamOptions.Builder()
                .apiKey("UcS6_obXxTLBZwwZcxlgJk40MDyc475pW5GHL94uIIqw")
                .build();
        com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText service = new com.ibm.watson.developer_cloud.speech_to_text.v1.SpeechToText(iamOptions);
        service.setEndPoint("https://stream.watsonplatform.net/speech-to-text/api");
// Signed PCM AudioFormat with 16kHz, 16 bit sample size, mono
        line.start();
        AudioInputStream audio = new AudioInputStream(line);
        RecognizeOptions options = new RecognizeOptions.Builder()
                .interimResults(false)
//                .inactivityTimeout(1) // use this to stop listening when the speaker pauses, i.e. for 5s
                .audio(audio)
                .contentType(HttpMediaType.AUDIO_RAW + "; rate=" + sampleRate)
//                .languageCustomizationId(languageFrom)
                .model(voiceFrom)
                .build();
        final String[] transcript = {""};

        service.recognizeUsingWebSocket(options, new BaseRecognizeCallback() {
            @Override
            public void onTranscription(SpeechRecognitionResults speechResults) {

                for (int i = 0; i < speechResults.getResults().size(); i++) {
                    transcript[0] = transcript[0] + speechResults.getResults().get(i).getAlternatives().get(0).getTranscript();
                }

                try {
                    if(!(transcript[0].isEmpty() || transcript[0]==null)){
                        setMainTranscript(transcript[0]);
                    }else{
                        setMainTranscript("No Input Given");
                    }
                } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
                    e.printStackTrace();
                }
            }
        });


        System.out.println("Listening to your voice for the next " + SECONDS + "s... \nPlease try to speak as clearly as possible");

        Thread.sleep(SECONDS * 1000);

        // closing the WebSockets underlying InputStream will close the WebSocket itself.
        line.stop();
        line.close();
    }

    private static void setMainTranscript(String transcript) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        System.out.println("Here's what we heard:\n" + transcript);
        TextTranslator.translate(languageFrom, languageTo, transcript, voiceTo);
    }

    private void defineLanguageFromOptions() {
        StringBuilder buildlanguageFromOptions = new StringBuilder();
        buildlanguageFromOptions.append("Hello and welcome to Speech to Speech Translator (v1.0) By Bradley Lamitie!\nPlease choose what language you will be translating from using the following options:\nChoose the language by typing an integer and hitting the return key:  ");
        String[] languages = {"English", "Chinese", "Spanish", "Arabic", "Portuguese", "Japanese", "German", "Korean", "French"};
            for (int i = 0; i < languages.length; i++) {
                buildlanguageFromOptions.append((i + 1) + ": " + languages[i]+ " \n  ");
            }
        translationInfo.setLanguageFromOptions( buildlanguageFromOptions.toString());
    }

    private static void defineVoiceOptions(String languageToChosen){
        StringBuilder buildVoiceFromOptions = new StringBuilder();
        buildVoiceFromOptions.append("Who would you like to speak the translation? \nChoose the speaker by typing an integer and hitting the return key: ");
        ArrayList<Pair> voicePairs = TextToSpeechTranslator.getVoiceList(TextToSpeechTranslator.service);
        ArrayList<String> voiceChoices= new ArrayList<>();
        for(int i = 0; i < voicePairs.size(); i++){
            String voiceName = voicePairs.get(i).getKey().toString().substring(0,2);
            if(languageToChosen.equalsIgnoreCase(voiceName)){
                voiceChoices.add(voicePairs.get(i).getValue().toString());
                chosenIndex.add(i);
            }
        }
        for (int i = 0; i < voiceChoices.size(); i++) {
            buildVoiceFromOptions.append((i + 1) + ": " + voiceChoices.get(i) + "\n  ");
        }

        translationInfo.setVoiceToOptions(buildVoiceFromOptions.toString());
    }


    private static String defineVoiceModelFrom(String translateFrom){
        switch(translateFrom){
            case "pt":
                return "pt-BR_NarrowbandModel";
            case "en":
                return "en-US_NarrowbandModel";
            case "ja":
                return "ja-JP_NarrowbandModel";
            case "ar":
                return "ar-AR_BroadbandModel";
            case "es":
                return "es-ES_NarrowbandModel";
            case "zh":
                return "zh-CN_NarrowbandModel";
            case "de":
                return "de-DE_BroadbandModel";
            case "ko":
                return "ko-KR_NarrowbandModel";
            case "fr":
                return "fr-FR_BroadbandModel";
            default:
                return "en-US_NarrowbandModel";
        }
    }

    @GetMapping("/translateFrom")
    public String translateFromForm(Model model) {
        model.addAttribute("translationInfo", translationInfo);
        defineLanguageFromOptions();

        return "translateFrom";
    }

    @PostMapping("/translateFrom")
    public String translateFromSubmit(@ModelAttribute TranslationInfo translationInfo) {
        languageFrom = translationInfo.getLanguageFrom();
        System.out.println("languageFrom: " +languageFrom);

        return "translateTo";
    }

    @GetMapping("/translateTo")
    public String translateToForm(Model model) {
        model.addAttribute("translationInfo", translationInfo);
        String temp = translationInfo.convertLanguageToInt(languageFrom);
        translationInfo.setLanguageFrom(temp);
        System.out.println("languageTo: " +languageTo);
        return "translateTo";
    }

    @PostMapping("/translateTo")
    public String translateToSubmit(@ModelAttribute TranslationInfo translationInfo) {
        defineVoiceOptions(languageTo);

        return "voiceTo";
    }

    @GetMapping("/voiceTo")
    public String voiceToForm(Model model) {
        return "voiceTo";
    }

    @PostMapping("/voiceTo")
    public String voiceToSubmit(@ModelAttribute TranslationInfo translationInfo) throws LineUnavailableException, InterruptedException {
        start();
        return "recording";
    }

    @GetMapping("/error")
    public String showErrorPage(Model model) {
        return "error";
    }

}

