import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by Hakan Kara on 27/10/2021
 */

public class OkeyGame extends JFrame {

    private static final int PANEL_WIDTH = 1600;
    private static final int PANEL_HEIGHT = 1200;
    private static final int BOARD_SPACE = 30;
    private static final String IMAGE_PATH = "/resources/Stones.png";
    private static final int SPACE_BTWN_STONES = 90;
    private static final int STONE_WIDTH = 70;
    private static final int STONE_HEIGHT = 100;

    // Properties
    private OkeyGameController okeyGameController;
    private JPanel mainPanel;
    private BufferedImage[] stoneImages;
    private Font defaultFont;

    // Constructor
    public OkeyGame() {

        okeyGameController = new OkeyGameController();
        defaultFont = new Font("Verdana", Font.BOLD, 16);
        setTitle("First Assignment");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        getStoneImages();
        createPanel();
        pack();
        setVisible(true);
    }

    // Methods
    public void getStoneImages() {
        try {
            BufferedImage image = ImageIO.read(getClass().getResource(IMAGE_PATH));
            stoneImages = new BufferedImage[Stone.NUMBER_OF_STONES];

            int x = 0;
            int y = 0;

            for (int i = 0; i < stoneImages.length; i++) {
                stoneImages[i] = image.getSubimage(x, y, STONE_WIDTH, STONE_HEIGHT);
                x += STONE_WIDTH;
                if ((i + 1) % Stone.NUMBER_OF_EACH_TYPE == 0) {
                    y += STONE_HEIGHT;
                    x = 0;
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void createPanel() {
        mainPanel = new JPanel();
        mainPanel.setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        mainPanel.setLayout(new GridLayout(OkeyGameController.NUMBER_OF_PLAYERS + 2, 1));

        Icon jokerIcon = new ImageIcon(stoneImages[okeyGameController.getJokerStone().getStoneId()]);

        JLabel jokerLabel = new JLabel(jokerIcon);
        jokerLabel.setText("Joker Stone");
        jokerLabel.setVerticalTextPosition(SwingConstants.BOTTOM);
        jokerLabel.setHorizontalTextPosition(SwingConstants.CENTER);
        jokerLabel.setFont(defaultFont);
        mainPanel.add(jokerLabel);

        for (int i = 0; i < OkeyGameController.NUMBER_OF_PLAYERS; i++) {
            JLabel playerLabel = new JLabel(" Player " + (i + 1) + ":");
            playerLabel.setFont(defaultFont);
            playerLabel.setVerticalTextPosition(SwingConstants.TOP);
            BoardPanel boardPanel = new BoardPanel(i);
            boardPanel.setLayout(new SpringLayout());
            boardPanel.add(playerLabel);
            mainPanel.add(boardPanel);
        }

        List<Integer> winnerIds = okeyGameController.getWinnerIds();
        JLabel winnerLabel = new JLabel();

        StringBuilder winnerText = new StringBuilder("Player(s) ");

        for (int i = 0; i < winnerIds.size() - 1; i++) {
            int playerNo = winnerIds.get(i) + 1;
            winnerText.append(playerNo).append(", ");

        }

        winnerText.append(winnerIds.get(winnerIds.size() - 1) + 1);
        winnerText.append(" got the best hand!");
        winnerLabel.setText(winnerText.toString());
        winnerLabel.setFont(defaultFont);
        winnerLabel.setHorizontalAlignment(SwingConstants.CENTER);
        mainPanel.add(winnerLabel);

        add(mainPanel);
    }

    class BoardPanel extends JPanel {

        private int playerId;

        public BoardPanel(int playerId) {
            if (playerId < OkeyGameController.NUMBER_OF_PLAYERS) {
                this.playerId = playerId;
            }
        }

        @Override
        public void paintComponent(Graphics g) {

            super.paintComponent(g);

            List<Stone> playerHand = okeyGameController.getPlayerHand(playerId);

            int x = 0;
            for (int i = 0; i < playerHand.size(); i++) {
                Stone stone = playerHand.get(i);
                int stoneId = stone.getStoneId();
                x += SPACE_BTWN_STONES;
                g.drawImage(stoneImages[stoneId], x, BOARD_SPACE, this);
            }

        }

    }

    public static void main(String[] args) {
        new OkeyGame();
    }
}





