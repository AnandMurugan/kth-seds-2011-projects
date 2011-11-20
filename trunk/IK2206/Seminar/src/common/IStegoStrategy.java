package common;

public interface IStegoStrategy {
	public void encode(IContainerFile cover, IReadStegoFile stego);
	public void decode(IContainerFile cover, IWriteStegoFile stego);
}
