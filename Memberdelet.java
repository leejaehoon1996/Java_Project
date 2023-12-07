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

public class Memberdelet extends Frame implements ActionListener{
	
	String url = "jdbc:mysql://localhost:3306/java_db?useUnicode=true&characterEncoding=utf8";
	String id = "root";
	String pass = "qwer";
	private Connection dc;
	
	Font font20 = new Font("TimesRoman", Font.BOLD, 20);
	Font font15 = new Font("TimesRoman", Font.BOLD, 15);
	Font font13 = new Font("TimesRoman", Font.BOLD, 13);
	Font font12b = new Font("TimesRoman", Font.BOLD, 12);
	
	Label Title = new Label("회원탈퇴");
	Label Title2 = new Label("삭제할 고객 정보");
	Label joinid = new Label("아이디 : ");
	TextField idt = new TextField();
	Label pw = new Label("비밀번호 : ");
	TextField pwt = new TextField();
	Label name = new Label("이름 : ");
	Label namet = new Label();
	Label mail = new Label("E-mail : ");
	Label mailt = new Label();
	Label phone = new Label("전화번호 : ");
	Label phonet = new Label();
	Button src = new Button("검색");
	Button del = new Button("삭제하기");
	Button close  = new Button("닫기");
	
	Memberdelet(){
		super("회원탈퇴");
		this.setSize(350, 450);
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
		this.add(Title); this.add(Title2); this.add(joinid); this.add(idt); this.add(pw); this.add(pwt); this.add(src);
		this.add(name); this.add(namet); 
		this.add(mail); this.add(mailt); this.add(phone); this.add(phonet); this.add(del); this.add(close);
		
		Title.setFont(font15); Title2.setFont(font13); name.setFont(font13); mail.setFont(font13); phone.setFont(font13);
		pw.setFont(font13);
		
		Title.setBounds(130, 50, 200, 30); Title2.setBounds(110, 170, 200, 30);
		
		joinid.setBounds(30, 90, 50, 30); idt.setBounds(80, 90, 80, 30);
		pw.setBounds(170, 90, 70, 30); pwt.setBounds(240, 90, 80, 30);
		src.setBounds(50, 130, 250, 30);
		name.setBounds(60, 200, 70, 30); namet.setBounds(140, 200, 90, 30);
		mail.setBounds(50, 240, 70, 30); mailt.setBounds(140, 240, 90, 30);
		phone.setBounds(50, 280, 70, 30); phonet.setBounds(140, 280, 90, 30);
		del.setBounds(130, 320, 80, 30);
		close.setBounds(140, 360, 60, 30);
		
	}
	void close() {this.setVisible(false);}
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
			String joinid = idt.getText();
			String pw = pwt.getText();
			if(joinid.equals("")) {msgDlg("아이디을 입력하세요"); return;}
			if(pw.equals("")) {msgDlg("비밀번호을 입력하세요"); return;}
			
			Search(joinid, pw);
		}
		else if(e.getSource()==del) {
			String joinid = idt.getText();
			String pw = pwt.getText();
			if(joinid.equals("")) {msgDlg("아이디을 입력하세요"); return;}
			if(pw.equals("")) {msgDlg("비밀번호을 입력하세요"); return;}
			delet(joinid, pw);
			msgDlg("삭제가 완료되었습니다");
			namet.setText("");
			mailt.setText("");
			phonet.setText("");
		}
		else if(e.getSource()==close) {close();}
		
	}
	
	void Search(String joinid, String pw){
		boolean check=false;
		String query = "select * from tb_javaproject where id = ? and pw = ?";
		
		try {
			PreparedStatement pstmt = dc.prepareStatement(query);
			pstmt.setString(1, joinid);
			pstmt.setString(2, pw);
			ResultSet rs = pstmt.executeQuery();
			if (rs.next()) {
				check=true;
				namet.setText(rs.getString("name"));
				mailt.setText(rs.getString("email"));
				phonet.setText(rs.getString("phone"));
			}
			rs.close();
			pstmt.close();
		} catch (SQLException ee) {
			System.err.println("login 처리 실패!!");
		}
		if(check==false) {msgDlg("존재하지 않는 아이디");}
	}
	
	void delet(String joinid, String pw) {
		String query = "delete from tb_javaproject where id = ? and pw = ?";
		
		try {
			PreparedStatement pstmt = dc.prepareStatement(query);
			pstmt.setString(1, joinid);
			pstmt.setString(2, pw);
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
