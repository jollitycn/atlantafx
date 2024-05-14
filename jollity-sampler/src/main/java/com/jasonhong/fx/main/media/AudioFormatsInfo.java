package com.jasonhong.fx.main.media;

import javax.sound.sampled.*;
  
public class AudioFormatsInfo {  
    public static void main(String[] args) {  
        try {  
            Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();  
              
            for (Mixer.Info mixerInfo : mixerInfos) {  
                Mixer mixer = AudioSystem.getMixer(mixerInfo);  
                  
                // 假设我们只关心目标行（即音频输出设备）  
               // Line.Info[] targetLineInfos = mixer.getTargetLineInfo();
                Line.Info[] targetLineInfos = mixer.getSourceLineInfo();
                for (Line.Info lineInfo : targetLineInfos) {  
                    Line line;  
                      
                    try {  
                        line = mixer.getLine(lineInfo);  
                          
                        if (line instanceof DataLine) {  
                            DataLine dataLine = (DataLine) line;  
                            AudioFormat format = dataLine.getFormat();

                                System.out.println("Supported Audio Format:");  
                                System.out.println("  Encoding: " + format.getEncoding());  
                                System.out.println("  Sample Rate: " + format.getSampleRate()); // 这就是采样率  
                                System.out.println("  Sample Size in Bits: " + format.getSampleSizeInBits());  
                                System.out.println("  Channels: " + format.getChannels());  
                                System.out.println("  Endian: " + (format.isBigEndian() ? "Big" : "Little"));  
//                                System.out.println("  Signed: " + (format.getSigned() ? "Signed" : "Unsigned"));
                                System.out.println("---------------------------------");  
                            }
                    } catch (LineUnavailableException e) {  
                        e.printStackTrace();  
                    }  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
}