package edu.wpi.cs3733.D21.teamB.util.tts;

import marytts.LocalMaryInterface;
import marytts.MaryInterface;
import marytts.exceptions.MaryConfigurationException;
import marytts.exceptions.SynthesisException;
import marytts.modules.synthesis.Voice;
import marytts.signalproc.effects.AudioEffect;
import marytts.signalproc.effects.AudioEffects;

import javax.sound.sampled.AudioInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class TextToSpeech {
    private AudioPlayer tts;
    private MaryInterface marytts;

    public TextToSpeech() {
        try {
            // Suppress System.out
            PrintStream printStream = System.out;
            System.setOut(new PrintStream(new OutputStream() {
                public void write(int b) {
                    // NO-OP
                }
            }));
            marytts = new LocalMaryInterface();
            System.setOut(printStream);

        } catch (MaryConfigurationException e) {
            e.printStackTrace();
        }
    }

    public void speak(String text, float gainValue, boolean daemon, boolean join) {
        stopSpeaking();
        try (AudioInputStream audio = marytts.generateAudio(text)) {
            tts = new AudioPlayer();
            tts.setAudio(audio);
            tts.setGain(gainValue);
            tts.setDaemon(daemon);
            tts.start();
            if (join) {
                tts.join();
            }
        } catch (SynthesisException | IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            tts.interrupt();
        }
    }

    public void stopSpeaking() {
        if (tts != null) {
            tts.cancel();
        }
    }

    public Collection<Voice> getAvailableVoices() {
        return Voice.getAvailableVoices();
    }

    public MaryInterface getMarytts() {
        return marytts;
    }

    public List<AudioEffect> getAudioEffects() {
        return StreamSupport.stream(AudioEffects.getEffects().spliterator(), false).collect(Collectors.toList());
    }

    public void setVoice(String voice) {
        marytts.setVoice(voice);
    }
}
