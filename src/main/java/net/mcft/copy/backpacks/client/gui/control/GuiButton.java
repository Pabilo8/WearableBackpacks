package net.mcft.copy.backpacks.client.gui.control;

import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;

import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import net.mcft.copy.backpacks.client.gui.GuiElementBase;

@SideOnly(Side.CLIENT)
public class GuiButton extends GuiElementBase {
	
	public static final ResourceLocation BUTTON_TEX = new ResourceLocation("textures/gui/widgets.png");
	public static final int DEFAULT_WIDTH  = 200;
	public static final int DEFAULT_HEIGHT = 20;
	public static final int MIN_TEXT_PADDING = 6;
	public static final int DEFAULT_TEXT_PADDING = 20;

	private String _text = "";
	private Runnable _action = null;
	
	
	public GuiButton() { this(DEFAULT_WIDTH); }
	public GuiButton(int width) { this(width, DEFAULT_HEIGHT); }
	public GuiButton(int width, int height) { this(width, height, ""); }
	
	public GuiButton(String text)
		{ this(getStringWidth(text) + DEFAULT_TEXT_PADDING, text); }
	public GuiButton(int width, String text)
		{ this(width, DEFAULT_HEIGHT, text); }
	public GuiButton(int width, int height, String text)
		{ this(0, 0, width, height, text); }
	
	public GuiButton(int x, int y, int width, int height, String text) {
		setPosition(x, y);
		setSize(width, height);
		setText(text);
	}
	
	
	public String getText() { return _text; }
	public void setText(String value) {
		if (value == null) throw new NullPointerException("Argument can't be null");
		_text = value;
	}
	public final void addText(String value) {
		if (value == null) throw new NullPointerException("Argument can't be null");
		setText(getText() + value);
	}
	
	public void setAction(Runnable value) { _action = value; }
	
	/** Called when this button is pressed. */
	public void onButtonClicked()
		{ if (_action != null) _action.run(); }
	
	public void playPressSound() {
		getMC().getSoundHandler().playSound(
			PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
	}
	
	@Override
	public boolean onMouseDown(int mouseButton, int mouseX, int mouseY) {
		if (mouseButton == MouseButton.LEFT) {
			playPressSound();
			onButtonClicked();
			return true;
		} else return false;
	}
	
	@Override
	public void draw(int mouseX, int mouseY, float partialTicks) {
		boolean isHighlighted = (isPressed() || contains(mouseX, mouseY));
		
		int ty = 46 + (isHighlighted ? 2 : 1) * 20;
		GuiUtils.drawContinuousTexturedBox(BUTTON_TEX, 0, 0, 0, ty, getWidth(), getHeight(), 200, 20, 2, 3, 2, 2, 0);
		
		drawWhateverIsOnTheButton(mouseX, mouseY, partialTicks);
	}
	
	/** Draws whatever is on the button. Yay. */
	public void drawWhateverIsOnTheButton(int mouseX, int mouseY, float partialTicks) {
		String text = getText();
		if (text.isEmpty()) return;
		
		FontRenderer fontRenderer = getFontRenderer();
		boolean isHighlighted = (isPressed() || contains(mouseX, mouseY));
		
		int textWidth = fontRenderer.getStringWidth(text);
		int maxTextWidth = getWidth() - MIN_TEXT_PADDING;
		if ((textWidth > maxTextWidth) && (textWidth > ELLIPSIS_WIDTH)) {
			text = fontRenderer.trimStringToWidth(text, maxTextWidth - ELLIPSIS_WIDTH).trim() + ELLIPSIS;
			textWidth = fontRenderer.getStringWidth(text);
		}
		
		int textColor = (isHighlighted ? 0xFFFFA0 : 0xE0E0E0); // Disabled color: 0xA0A0A0
		fontRenderer.drawStringWithShadow(text, getWidth() / 2 - textWidth / 2, (getHeight() - 8) / 2, textColor);
	}
	
}