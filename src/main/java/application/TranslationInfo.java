/**
 * file: SpeechToSpeechTranslatorApp.java
 * author: Bradley Lamitie
 * course: MSCS 621
 *
 * This file contains the declaration of the SpeechToSpeechTranslatorApp class
 */

package application;

import com.ibm.watson.developer_cloud.language_translator.v3.util.Language;
import javafx.util.Pair;

import java.util.ArrayList;

import static application.TranslatorController.*;

public class TranslationInfo {

   public String getLanguageFrom() {
       return languageFrom;
   }

   public void setLanguageFrom(String languageChosen) {
       languageFrom = languageChosen;
       int number = Integer.parseInt(languageChosen) - 1;
       switch (number) {
           case 0:
               languageFrom = Language.ENGLISH;
               break;
           case 1:
               languageFrom = Language.CHINESE;
               break;
           case 2:
               languageFrom = Language.SPANISH;
               break;
           case 3:
               languageFrom = Language.ARABIC;
               break;
           case 4:
               languageFrom = Language.PORTUGUESE;
               break;
           case 5:
               languageFrom = Language.JAPANESE;
               break;
           case 6:
               languageFrom = Language.GERMAN;
               break;
           case 7:
               languageFrom = Language.KOREAN;
               break;
           case 8:
               languageFrom = Language.FRENCH;
               break;
       }
//        System.out.println("language From = " + languageFrom);
   }

   public String getLanguageTo() {
       return languageTo;
   }

   public void setLanguageTo(String languageChosen) {
       System.out.println(languageFrom);
       System.out.println("language to set");
       String[] modelArray = {"en-es","en-pt","en-ja","en-fr","en-it","en-de", "ja-en", "pt-en", "ar-en", "es-en", "es-fr", "zh-en", "de-en", "de-fr", "de-it", "ko-en", "fr-en", "fr-de", "fr-es"};
       ArrayList<String> availableLanguagesTo = new ArrayList<>();
       int chosenValue = Integer.parseInt(languageChosen);
       for(String modelID: modelArray){
           if(modelID.substring(0,2).equalsIgnoreCase(languageFrom)){
               availableLanguagesTo.add(modelID.substring(3,5));
           }
       }
       switch (availableLanguagesTo.get(chosenValue - 1)) {
           case "es":
               languageTo = Language.SPANISH;
               break;
           case "pt":
               languageTo = Language.PORTUGUESE;
               break;
           case "ja":
               languageTo = Language.JAPANESE;
               break;
           case "en":
               languageTo = Language.ENGLISH;
               break;
           case "fr":
               languageTo = Language.FRENCH;
               break;
           case "it":
               languageTo = Language.ITALIAN;
               break;
           case "de":
               languageTo = Language.GERMAN;
               break;
       }
   }

   public static String convertLanguageToInt(String language){
       switch (language) {
           case "en":
               return "1";
           case "zh":
               return "2";
           case "es":
               return "3";
           case "ar":
               return "4";
           case "pt":
               return "5";
           case "ja":
               return "6";
           case "de":
               return "7";
           case "ko":
               return "8";
           case "fr":
               return "9";
           default:
               return null;
       }
   }
   public String getVoiceTo() {
       return voiceTo;
   }

   public void setVoiceTo(String voicePicked) {
       int voiceChosen = Integer.parseInt(voicePicked) - 1;
       ArrayList<Pair> voicePairs = TextToSpeechTranslator.getVoiceList(TextToSpeechTranslator.service);
       String chosenVoice = voicePairs.get(chosenIndex.get(voiceChosen)).getKey().toString();
       String chosenSpeaker = voicePairs.get(chosenIndex.get(voiceChosen)).getValue().toString();
       chosenVoice = chosenVoice + "_" + chosenSpeaker.substring(0, chosenSpeaker.indexOf(":")) + "Voice";
       System.out.println("voice To options set" + "\n " + chosenVoice);
       voiceTo = chosenVoice;
   }

   public String getLanguageFromOptions() {
       return languageFromOptions;
   }

   public void setLanguageFromOptions(String languageOptions) {
       System.out.println("language from options set");
       languageFromOptions = languageOptions;
   }

   public String getLanguageToOptions() {
       return defineLanguageToOptions();
   }

   public void setLanguageToOptions(String setlanguageOptions) {
       System.out.println("language to options set");
       languageToOptions = setlanguageOptions;
   }

   private String defineLanguageToOptions() {
       StringBuilder buildLanguageToOptions = new StringBuilder();
       buildLanguageToOptions.append("Please choose what language you will be translating to using the following options:\nChoose the language by typing an integer and hitting the return key:\n");
       ArrayList<String> languageList = new ArrayList<>();

       ArrayList<String> availableLanguagesTo = new ArrayList<>();
       String[] modelArray = {"en-es","en-pt","en-ja","en-fr","en-it","en-de", "ja-en", "pt-en", "ar-en", "es-en", "es-fr", "zh-en", "de-en", "de-fr", "de-it", "ko-en", "fr-en", "fr-de", "fr-es"};
//        System.out.println(languageFrom);
       for(String modelID: modelArray){
           if(modelID.substring(0,2).equalsIgnoreCase(languageFrom)){
               availableLanguagesTo.add(modelID.substring(3,5));
           }
       }
//        System.out.println(availableLanguagesTo.toString());

       for (String lang : availableLanguagesTo) {
           String language = "";
           if (lang.equalsIgnoreCase("es")) {
               language = "Spanish (Español)";
           } else if (lang.equalsIgnoreCase("pt")) {
               language = "Portuguese (Português)";
           } else if (lang.equalsIgnoreCase("ja")) {
               language = "Japanese (日本語)";
           } else if (lang.equalsIgnoreCase("en")) {
               language = "English";
           } else if (lang.equalsIgnoreCase("fr")) {
               language = "French (français)";
           } else if (lang.equalsIgnoreCase("it")) {
               language = "Italian (Italiano)";
           } else if (lang.equalsIgnoreCase("de")) {
               language = "German (Deutsch)";
           }
           if (!languageList.contains(language)) {
               languageList.add(language);
           }
       }
       for (int i = 0; i < languageList.size(); i++) {
           buildLanguageToOptions.append((i + 1) + ": " + languageList.get(i) + "\n  ");
       }

       return buildLanguageToOptions.toString();
   }

   public String getVoiceToOptions() {
       return voiceToOptions;
   }

   public void setVoiceToOptions(String voiceChoice) {
       System.out.println("language from options set");
       voiceToOptions = voiceChoice;
   }

}
