/**
 * file: TextToSpeechTranslator.java
 * author: Bradley Lamitie
 * course: MSCS 621
 *
 * This file contains the declaration of the TextToSpeechTranslator class
 */

package application;

import com.ibm.watson.developer_cloud.service.security.IamOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.TextToSpeech;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.SynthesizeOptions;
import com.ibm.watson.developer_cloud.text_to_speech.v1.model.Voices;
import com.ibm.watson.developer_cloud.text_to_speech.v1.util.WaveUtils;
import javafx.util.Pair;

import javax.sound.sampled.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * TextToSpeechTranslator
 *
 * This class uses the Extended Euclidean Algorithm to pass in two integers,
 * a and b, to find d, x, and y such that d = gcd(a,b) and d = ax + by.
 */
class TextToSpeechTranslator {
    private static Clip clip;

    private static final String filePath = "hello_world.wav";

    private static final IamOptions options = new IamOptions.Builder()
            .apiKey("Spcp0Fc5tx4kgZFn2rHe3bnr2qh9PLZXlg9tjNGdH06G")
            .build();

    static final TextToSpeech service = new TextToSpeech(options);


    public static void main(String[] args) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        textToSpeechTranslate("Hello", "es-ES_EnriqueVoice");
    }

    public static void textToSpeechTranslate(String translatedText, String voice) throws IOException, LineUnavailableException, UnsupportedAudioFileException {
        service.setEndPoint("https://stream.watsonplatform.net/text-to-speech/api");

        try {
            SynthesizeOptions synthesizeOptions =
                    new SynthesizeOptions.Builder()
                            .text(translatedText)
                            .accept("audio/wav")
                            .voice(voice)
                            .build();

            InputStream inputStream =
                    service.synthesize(synthesizeOptions).execute();
            InputStream in = WaveUtils.reWriteWaveHeader(inputStream);

            OutputStream out = new FileOutputStream("hello_world.wav");
            byte[] buffer = new byte[1024];
            int length;
            while ((length = in.read(buffer)) > 0) {
                out.write(buffer, 0, length);
            }

            out.close();
            in.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        playTranslatedSpeech();
    }

    private static void playTranslatedSpeech() throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        // create AudioInputStream object
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(TextToSpeechTranslator.filePath).getAbsoluteFile());

        // create clip reference
        clip = AudioSystem.getClip();

        // open audioInputStream to the clip
        clip.open(audioInputStream);

        try
        {


            play();
            Scanner sc = new Scanner(System.in);

            while (clip.getMicrosecondLength() > clip.getMicrosecondPosition())
            {
//                int c = sc.nextInt();
//                audioPlayer.gotoChoice(c);
//                System.out.println(c);

            }
        }

        catch (Exception ex)
        {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();

        }
    }

    // Method to play the audio
    private static void play()
    {
        //start the clip
        clip.start();
        TranslatorController.line.stop();
    }

    public static ArrayList<Pair> getVoiceList(TextToSpeech service){
        Voices voices = service.listVoices().execute();
        ArrayList<Pair> voiceList = new ArrayList<>();
        for(int i =0; i< voices.getVoices().size(); i++){
            voiceList.add(new Pair(voices.getVoices().get(i).getLanguage(), voices.getVoices().get(i).getDescription()));
        }
        return voiceList;
    }

}
