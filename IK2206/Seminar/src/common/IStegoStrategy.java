package common;

public interface IStegoStrategy {
	public IContainerFile encode(IContainerFile cover, IStegoFile stego);
	public IStegoFile decode(IContainerFile cover);
}
