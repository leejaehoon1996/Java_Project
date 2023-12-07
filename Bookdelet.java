package Java_project_1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Bookdelet extends Frame implements ActionListener{
	
	String url = "jdbc:mysql://localhost:3306/java_db?useUnicode=true&characterEncoding=utf8";
	String id = "root";
	String pass = "qwer";
	private Connection dc;
	
	Font font20 = new Font("TimesRoman", Font.BOLD, 20);
	Font font15 = new Font("TimesRoman", Font.BOLD, 15);
	Font font13 = new Font("TimesRoman", Font.BOLD, 13);
	Font font12b = new Font("TimesRoman", Font.BOLD, 12);
	
	Label Title = new Label("도서 정보 삭제");
	Label Title2 = new Label("삭제할 도서 정보");
	Label name = new Label("도서명 : ");
	TextField namet = new TextField();
	Label writer = new Label("저자 : ");
	Label writert = new Label();
	Label publish = new Label("출판사 : ");
	Label publisht = new Label();
	Label cord = new Label("분류코드 : ");
	Label cordt = new Label();
	Button src = new Button("검색");
	Button del = new Button("삭제하기");
	Button close  = new Button("닫기");
	
	Bookdelet(){
		super("도서관리 프로그램");
		this.setSize(350, 480);
		this.setLocation(700, 350);
		initi();
		start();
		this.setVisible(true);
		
		try {
			Class.forName("org.gjt.mm.mysql.Driver");
		} catch (ClassNotFoundException ee) {
			return;
		}
		try {
			dc = DriverManager.getConnection(url, id, pass);
		} catch (SQLException ee) {
		}
	}
	
	
	void initi() {
		this.setLayout(null);
		this.add(Title); this.add(Title2); this.add(name); this.add(namet); this.add(writer); this.add(writert); this.add(src);
		this.add(publish); this.add(publisht); this.add(cord); this.add(cordt);
		this.add(del); this.add(close);
		
		Title.setFont(font15); Title2.setFont(font13); name.setFont(font13); writer.setFont(font13); publish.setFont(font13); cord.setFont(font13);
		
		Title.setBounds(120, 50, 200, 30); Title2.setBounds(120, 160, 200, 30);
		name.setBounds(60, 100, 50, 30); namet.setBounds(130, 100, 100, 30);
		src.setBounds(230, 100, 80, 30);
		writer.setBounds(70, 200, 60, 30); writert.setBounds(140, 200, 90, 30);
		publish.setBounds(60, 250, 70, 30); publisht.setBounds(140, 250, 90, 30);
		cord.setBounds(60, 300, 70, 30); cordt.setBounds(140, 300, 90, 30);
		del.setBounds(130, 350, 80, 30);
		close.setBounds(140, 390, 60, 30);
		
	}
	void close() {this.setVisible(false);
				new Bookmain();}
	void start() {
		src.addActionListener(this); del.addActionListener(this); close.addActionListener(this);
		
		this.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				close();
			}
		});
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()==src) {
			String name = namet.getText();
			if(name.equals("")) {msgDlg("도서명을 입력하세요"); return;}
			
			Search(name);
		}
		else if(e.getSource()==del) {
			String name = namet.getText();
			String writer = writert.getText();
			String publish = publisht.getText();
			String cord = cordt.getText();
			
			if(name.equals("")) {msgDlg("도서명을 입력하세요"); return;}
			delet(name);
			msgDlg("수정이 완료되었습니다");
			namet.setText("");
			writert.setText("");
			publisht.setText("");
			cordt.setText("");
		}
		else if(e.getSource()==close) {close();}
		
	}
	
	void Search(String name){
		boolean check=false;
		String query = "select * from tb_javaproject1 where name = ?";
		
		try {
			PreparedStatement pstmt = dc.prepareStatement(query);
			pstmt.setString(1, name);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				check=true;
				namet.setText(rs.getString("name"));
				writert.setText(rs.getString("writer"));
				publisht.setText(rs.getString("publish"));
				cordt.setText(rs.getString("cord"));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException ee) {
			System.err.println("login 처리 실패!!");
		}
		if(check==false) {msgDlg("존재하지 않는 도서명");}
	}
	
	void delet(String name) {
			String query = "delete from tb_javaproject1 where name = ?";
			
			try {
				PreparedStatement pstmt = dc.prepareStatement(query);
				pstmt.setString(1, name);
				pstmt.executeUpdate();
				pstmt.close();
			} catch (SQLException ee) {
				System.err.println("회원 삭제 실패!!");
			}
		}
		
	void msgDlg(String msg)
	{
		//다이얼로그창
    	Dialog dlg = new Dialog(this, "알림창", true);
    	//컴포넌트선언
    	Button bt = new Button("확인");
    	Label lb = new Label(msg);
    	//레이아웃설정
		dlg.setLayout(null);
		//컴포넌트 다이얼로그에 등록
		dlg.add(bt); dlg.add(lb);
	
		//배치하기   		
		lb.setBounds(110, 70, 150, 30);
		bt.setBounds(140, 120, 100, 30);
		
		//이벤트등록
		bt.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dlg.setVisible(false);
			}
		});	
		//이벤트등록
		dlg.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				dlg.setVisible(false);
			}
		});
		dlg.setLocation(650, 400);
		dlg.setSize(400, 200);
		dlg.setVisible(true);
	}
	
}
