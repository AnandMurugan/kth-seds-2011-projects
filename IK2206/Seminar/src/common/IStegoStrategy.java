package common;

public interface IStegoStrategy {
	public IContainerFile encode(IContainerFile cover, IReadStegoFile stego);
	public IWriteStegoFile decode(IContainerFile cover);
}
