package demo.base.utils.utils;

public class WordMyUnits {
    public static final int EMU_PER_PX = 9525;
    public static final int emuToPx(double emu) {
        return (int) (emu/9525);
    }
}
