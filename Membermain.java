package Java_project_1;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Membermain extends Frame implements ActionListener{

	String url = "jdbc:mysql://localhost:3306/java_db?useUnicode=true&characterEncoding=utf8";
	String id = "root";
	String pass = "qwer";
	private Connection dc;
	
		Font font20 = new Font("TimesRoman", Font.BOLD, 20);
		Font font13 = new Font("TimesRoman", Font.BOLD, 13);
		Font font12 = new Font("TimesRoman", Font.BOLD, 12);
		
		
		Label name = new Label("이름 : ");
		Label joinid = new Label("아이디 : ");
		TextField namet = new TextField();
		TextField idt = new TextField();
		TextArea list = new TextArea();
		Button src  = new Button("검색");
		Button edit  = new Button("회원수정");
		Button del  = new Button("회원삭제");
		Button close  = new Button("닫기");
		
		Label info = new Label("검색된 회원정보");
		Label inname = new Label("이름 : ");
		TextField innamet = new TextField();
		Label inid = new Label("아아디 : ");
		TextField inidt = new TextField();
		Label mail = new Label("E-mail : ");
		TextField mailt = new TextField();
		Label phone = new Label("전화번호 : ");
		TextField phonet = new TextField();

		Membermain(){
			super("도서회원관리 프로그램");
			this.setSize(700, 460);
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
			DataLoad();
		}
		
		void initi() {
			this.setLayout(null);
			this.add(name); this.add(joinid); this.add(namet);  this.add(idt);
			this.add(src); this.add(list); this.add(edit); this.add(del); this.add(close);
			this.add(info); this.add(inname); this.add(innamet); this.add(inid); this.add(inidt);
			this.add(mail); this.add(mailt); this.add(phone); this.add(phonet); 
			
			
			name.setFont(font13); joinid.setFont(font13); info.setFont(font13); list.setFont(font12);
			
			name.setBounds(80, 50, 60, 30); namet.setBounds(140, 50, 80, 30);
			joinid.setBounds(280, 50, 80, 30); idt.setBounds(370, 50, 100, 30);
			src.setBounds(480, 50, 50, 30);
			list.setBounds(190, 100, 470, 280);
			edit.setBounds(180, 400, 100, 30); del.setBounds(310, 400, 100, 30);
			close.setBounds(600, 400, 50, 30);
			info.setBounds(40, 120, 110, 30);
			inname.setBounds(30, 160, 50, 30); innamet.setBounds(80, 160, 90, 25);
			inid.setBounds(20, 210, 60, 30); inidt.setBounds(80, 210, 90, 25);
			mail.setBounds(20, 260, 50, 30); mailt.setBounds(80, 260, 90, 25);
			phone.setBounds(15, 310, 60, 30); phonet.setBounds(80, 310, 90, 25);
			
	}
		void close() {this.setVisible(false);}
		void start() {
			src.addActionListener(this); edit.addActionListener(this); 
			del.addActionListener(this); close.addActionListener(this);
			
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
				String joinid = idt.getText();
				if(name.equals("")) {msgDlg("이름을 입력하세요"); return;}
				if(joinid.equals("")) {msgDlg("아이디을 입력하세요"); return;}
				Search(name, joinid);}
			
			else if(e.getSource()==edit) {ManageEdit edit = new ManageEdit(); close();}
			else if(e.getSource()==del) {Managedelet delet = new Managedelet(); close();}
			else if(e.getSource()==close) {close();}
		}
		
		void DataLoad(){
			
			Statement stmt = null; // 접속상태
			ResultSet rs = null; // db 주머니같은
			String query = "select * from tb_javaproject"; //쿼리목록
			try {
				stmt = dc.createStatement(); //접속한 상태값
				rs = stmt.executeQuery(query); //끌어와서 실행
				String data="";
				while (rs.next()) {
					data+=rs.getString("name")+"/"+rs.getString("id")+"/"+rs.getString("email")+"/"+rs.getString("phone")+"\n"+"\n";
				}
				list.setText(data);
				rs.close();
				stmt.close();
			} catch (SQLException ee) {
				System.err.println("error = " + ee.toString());
			}
			
		}

		void Search(String name, String joinid){
			boolean check=false;
			String query = "select * from tb_javaproject where name = ? and id = ?";
			
			try {
				PreparedStatement pstmt = dc.prepareStatement(query);
				pstmt.setString(1, name);
				pstmt.setString(2, joinid);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					check=true;
					innamet.setText(rs.getString("name"));
					inidt.setText(rs.getString("id"));
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

		public void itemStateChanged(ItemEvent e) {}
}
