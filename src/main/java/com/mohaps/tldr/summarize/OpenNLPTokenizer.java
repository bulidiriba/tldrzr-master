/*
 *  
 *  TL;DRzr - A simple algorithmic summarizer
 *  Website: http://tldrzr.com
 *  Author: Saurav Mohapatra (mohaps@gmail.com)
 *  
 *  Copyright (c) 2013, Saurav Mohapatra
 *  All rights reserved.
 *  
 *  
 *  
 *  Redistribution and use in source and binary forms, with or without modification, are permitted 
 *  provided that the following conditions are met:
 *  
 *  	a)  Redistributions of source code must retain the above copyright notice, 
 *  		this list of conditions and the following disclaimer.
 *  
 *  	b)  Redistributions in binary form must reproduce the above copyright notice, 
 *  		this list of conditions and the following disclaimer in the documentation 
 *  		and/or other materials provided with the distribution.
 *  	
 *  	c)  Neither the name of TL;DRzr nor the names of its contributors may be used 
 *  		to endorse or promote products derived from this software without specific 
 *  		prior written permission.
 *  
 *  THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, 
 *  BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT 
 *  SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL 
 *  DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) 
 *  HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 *  ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.mohaps.tldr.summarize;

import java.io.IOException;
import java.io.InputStream;

import com.mohaps.tldr.utils.Words;

import opennlp.tools.tokenize.Tokenizer;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;



/**
 * Uses the OpenNLP tokenizer (falls back to RegExTokenizer if it can't find the model files for OpenNLP)
 * @author mohaps
 *
 */
public class OpenNLPTokenizer implements ITokenizer {

	private static TokenizerModel TOKENIZER_MODEL;
	private static RegExTokenizer FALLBACK;
	static {
		try { 
			InputStream inputFile = Words.class.getClassLoader().getResourceAsStream("en-token.bin");
			if(inputFile != null) {
				try {
					TOKENIZER_MODEL = new TokenizerModel(inputFile);
					System.out.println(">> OpenNLP Tokenizer Model loaded!");
				} finally {
					if(inputFile != null) {
						try { inputFile.close(); } catch (Throwable t){}
					}
				}
			}
		} catch (IOException ex) {
			System.err.println("Failed to load token model for OpenNLP. error = "+ex.getLocalizedMessage()+". Will fall back to regex based token parsing");
			ex.printStackTrace();
		} finally {
			if(TOKENIZER_MODEL == null) {
				FALLBACK = new RegExTokenizer();
			}
		}
	}

	public String[] tokenize(String input) throws Exception {
		if(TOKENIZER_MODEL != null) {
			Tokenizer tokenizer = new TokenizerME(TOKENIZER_MODEL);
			return tokenizer.tokenize(input);
		} else {
			return FALLBACK.tokenize(input);
		}
	}

}
