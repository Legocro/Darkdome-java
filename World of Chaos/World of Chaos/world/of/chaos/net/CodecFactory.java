package world.of.chaos.net;

/**
 *
 * @author Loki
 */

import org.apache.mina.filter.codec.ProtocolCodecFactory;
import org.apache.mina.filter.codec.ProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolEncoder;

public class CodecFactory implements ProtocolCodecFactory {

	/**
	 * The encoder.
	 */
	private final ProtocolEncoder encoder = new RS2ProtocolEncoder();

	/**
	 * The decoder.
	 */
	private final ProtocolDecoder decoder = new RS2LoginProtocolDecoder();

	@Override
	/**
	 * Get the encoder.
	 */
	public ProtocolEncoder getEncoder() throws Exception {
		return encoder;
	}

	@Override
	/**
	 * Get the decoder.
	 */
	public ProtocolDecoder getDecoder() throws Exception {
		return decoder;
	}

}

