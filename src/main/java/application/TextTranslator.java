/**
 * file: SpeechToSpeechTranslatorApp.java
 * author: Bradley Lamitie
 * course: MSCS 621
 *
 * This file contains the declaration of the SpeechToSpeechTranslatorApp class
 */

package application;

import com.ibm.watson.developer_cloud.language_translator.v3.LanguageTranslator;
import com.ibm.watson.developer_cloud.language_translator.v3.model.TranslateOptions;
import com.ibm.watson.developer_cloud.language_translator.v3.model.TranslationResult;
import com.ibm.watson.developer_cloud.language_translator.v3.util.Language;
import com.ibm.watson.developer_cloud.service.security.IamOptions;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;

class TextTranslator {
    public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        translate(Language.ENGLISH, Language.SPANISH, "", "es-ES_EnriqueVoice");
    }

    public static void translate(String languageFrom, String languageTo, String text, String voice) throws UnsupportedAudioFileException, LineUnavailableException, IOException {
        LanguageTranslator service = new LanguageTranslator("2018-12-13");
        IamOptions iamOptions = new IamOptions.Builder()
                .apiKey("cMMQWQpZ95zJYs9xQ6n4tIHvn6blOlD17th9tXCE15UY")
                .build();
        service.setEndPoint("https://gateway.watsonplatform.net/language-translator/api");

        service.setIamCredentials(iamOptions);

        TranslateOptions translateOptions = new TranslateOptions.Builder()
                .addText(text)
                .source(languageFrom)
                .target(languageTo)
                .build();
        TranslationResult translationResult = service.translate(translateOptions).execute();
        String translatedText = (translationResult.getTranslations().get(0).getTranslationOutput());
        TextToSpeechTranslator.textToSpeechTranslate(translatedText, voice);
    }
}
