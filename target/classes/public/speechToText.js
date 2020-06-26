function speechToText(){
    'use strict';

    var SpeechToTextV1 = require('watson-developer-cloud/speech-to-text/v1');
    var LineIn = require('line-in'); // the `mic` package also works - it's more flexible but requires a bit more setup
    var wav = require('wav');

    var speechToText = new SpeechToTextV1({
      iam_apikey: 'UcS6_obXxTLBZwwZcxlgJk40MDyc475pW5GHL94uIIqw',
      url: 'https://stream.watsonplatform.net/speech-to-text/api'
    });

    var lineIn = new LineIn(); // 2-channel 16-bit little-endian signed integer pcm encoded audio @ 44100 Hz

    var wavStream = new wav.Writer({
      sampleRate: 44100,
      channels: 2,
    });

    var recognizeStream = speechToText.recognizeUsingWebSocket({
      content_type: 'audio/wav',
      interim_results: true,
    });

    lineIn.pipe(wavStream);

    wavStream.pipe(recognizeStream);

    recognizeStream.pipe(process.stdout);
}