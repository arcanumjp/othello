package jp.arcanum.othello.page.main;



public interface GameStatus {

	public final int NONE = 0;				// ゲームしていない
	public final int GAMING = 1;			// ゲーム中
	public final int GAMEOVER_DRAW = 4;		// ゲームオーバー（引き分け）
	public final int GAMEOVER_1WIN = 5;		// ゲームオーバー（１WIN）
	public final int GAMEOVER_2WIN = 6;		// ゲームオーバー（２WIN）

}
