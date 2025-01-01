//made by OneDop
package myHangMan;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class HangMan extends Application{
	ArrayList<Text> T = new ArrayList<Text>();
	List<StackPane> lettercells = new ArrayList<StackPane>();
	Random rand = new Random();
	String[] words = {"jordan","pizza","new york","qais","tree","group","firefly","dream","qoute","police officer","house","tic tac toe"};
	String word =null;
	Shape[] man = new Shape[6];
	int Wcnt=0;
	int Ccnt=0;
	int Wlen = 0;
	Button btn = new Button("replay");
	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage ps) throws Exception {
		ps.setTitle("404 Hangman");
		ps.setHeight(650);
		ps.setWidth(550);
		
		newGame();
		
		btn.setDisable(true);
		
		Pane stand = new Pane();
		Line[] Stand = new Line[4];
		Stand[0] = new Line(120,230,310,230);
		Stand[1] = new Line(140,10,140,230);
		Stand[2] = new Line(140,10,250,10);
		Stand[3] = new Line(250,10,250,30);
		man[0] = new Circle(250,55,25);
		man[2] = new Line(250,80,200,130);
		man[3] = new Line(250,80,300,130);
		man[1] = new Line(250,80,250,150);
		man[4] = new Line(250,150,300,200);
		man[5] = new Line(250,150,200,200);
		
		for(Line l : Stand)
		{
			stand.getChildren().add(l);
		}
		
		for(Shape s : man)
		{
			s.setVisible(false);
			stand.getChildren().add(s);
		}
		
		HBox hb = new HBox(10);
		HBox hb2 = new HBox(10);
		hb.setAlignment(Pos.CENTER);
		hb2.setAlignment(Pos.CENTER);
		
		initializeWord(hb, hb2);
		
		GridPane letters = LettersGrid(ps.getWidth()/13);
		
		Rectangle space = new Rectangle(200,50);
		space.setVisible(false);
		Rectangle space2 = new Rectangle(200,50);
		space2.setVisible(false);
		Rectangle space3 = new Rectangle(200,30);
		space3.setVisible(false);
		VBox vb = new VBox(space3,stand,space,hb,hb2,space2,letters,btn);
		vb.setAlignment(Pos.TOP_CENTER);
		
		btn.setOnAction(event ->{
			newGame();
			initializeWord(hb, hb2);
			resetManShapes();
			resetLetterCells();
			
			GridPane newLettersGrid = LettersGrid(ps.getWidth() / 13);
			VBox tmp = (VBox) ps.getScene().getRoot();
		    tmp.getChildren().remove(vb.getChildren().size() - 2);
		    tmp.getChildren().add(vb.getChildren().size() - 1, newLettersGrid);
		    
			btn.setDisable(true);
		});
		
		ps.setScene(new Scene(vb));
		ps.show();
	}
	void newGame()
	{
		Ccnt=0;
		Wcnt=0;
		Wlen=0;
		T.clear();
		lettercells.clear();
		word = words[rand.nextInt(words.length)].toUpperCase();
	}
	
	void initializeWord(HBox hb, HBox hb2)
	{
		 hb.getChildren().clear();
		 hb2.getChildren().clear();
		for(int i =0;i<word.length();i++)
		{	
			Text t = new Text(word.charAt(i)+"");
			t.setFont(Font.font(35));
			t.setVisible(false);
			T.add(t);
			hb.getChildren().add(t);
			if(word.charAt(i) != ' ') {
			Wlen++;
			Text t2 = new Text("-");
			t2.setFont(Font.font(50));
			hb2.getChildren().add(t2);
			}
			else
			{
			Text t3 = new Text(" ");
			t3.setFont(Font.font(35));
			hb2.getChildren().add(t3);
			}
		}
	}
	
	GridPane LettersGrid(double w)
	{
		GridPane letters = new GridPane();
		letters.setHgap(2);
		letters.setVgap(10);
		letters.setAlignment(Pos.CENTER);
		for(int i =65; i<78;i++) {
		StackPane cell = makeCell((char)i, w);
		letters.add(cell,i-65,0);
		}
		for(int i =78; i<91;i++) {
		StackPane cell = makeCell((char)i,w);
		letters.add(cell,i-78,1);
		}
		return letters;
	}
	
	void resetManShapes() {
	    for (Shape s : man) {
	        s.setVisible(false);
	    }
	}

	void resetLetterCells() {
		for (StackPane cell : lettercells) {
	        cell.setDisable(false);
	        Text t = (Text) cell.getChildren().get(0);
	        t.setFill(Color.BLACK);
	    }
	}
	
	StackPane makeCell(char c, double w) {
	    Rectangle border = new Rectangle(35, 40);
	    border.setFill(null);
	    border.setStroke(Color.BLACK);
	    border.setStrokeWidth(2);

	    Text t = new Text(String.valueOf(c));
	    t.setFont(Font.font(w - 5));
	    t.setStyle("-fx-font-weight: bold;");

	    StackPane sp = new StackPane(t, border);
	    sp.setOnMouseClicked(event -> {
	        if (t.getFill() == Color.GRAY) {
	            return;
	        }
	        t.setFill(Color.GRAY);

	        boolean found = false;
	        for (int i = 0; i < word.length(); i++) {
	        	if (word.charAt(i) == c) {
	                if (!T.get(i).isVisible()) {
	                    T.get(i).setVisible(true);
	                    Ccnt++;
	                }
	                found = true;
	            }
	        }

	        if (found) {
	            checkWin();
	        } else {
	            wrong();
	        }
	    });

	    lettercells.add(sp);
	    return sp;
	}


	
	void wrong() {
		man[Wcnt].setVisible(true);
		Wcnt++;
		if(Wcnt==6)
		loosing();
	}
	
	void loosing() {
	        Alert alt = new Alert(AlertType.INFORMATION, "Looser!! the word is: " + word);
	        alt.show();
	        disableAllCells(); 
	        btn.setDisable(false);
	}
	
	void checkWin() {
	    if (Ccnt == Wlen) {
	        Alert alt = new Alert(AlertType.INFORMATION, "Congratulations! You guessed the word!");
	        alt.show();
	        disableAllCells(); 
	        btn.setDisable(false);
	    }
	}

	void disableAllCells() {
	    for (StackPane cell : lettercells) {
	        cell.setDisable(true);
	    }
	}
	}
