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

public class Bookmain extends Frame implements ActionListener{

	String url = "jdbc:mysql://localhost:3306/java_db?useUnicode=true&characterEncoding=utf8";
	String id = "root";
	String pass = "qwer";
	private Connection dc;
	
		Font font20 = new Font("TimesRoman", Font.BOLD, 20);
		Font font13 = new Font("TimesRoman", Font.BOLD, 13);
		Font font12 = new Font("TimesRoman", Font.BOLD, 12);
		
		
		Label name = new Label("도서명 : ");
		Label writer = new Label("저자명 : ");
		TextField namet = new TextField();
		TextField writert = new TextField();
		TextArea list = new TextArea();
		Button src  = new Button("검색");
		Button bookin  = new Button("도서등록");
		Button edit  = new Button("도서수정");
		Button del  = new Button("도서삭제");
		Button close  = new Button("닫기");
		
		Label info = new Label("검색된 도서정보");
		Label inname = new Label("도서명 : ");
		TextField innamet = new TextField();
		Label inwriter = new Label("저자 : ");
		TextField inwritert = new TextField();
		Label publish = new Label("출판사 : ");
		TextField publisht = new TextField();
		Label incord = new Label("분류코드 : ");
		TextField incordt = new TextField();
		Label datet = new Label();

		Bookmain(){
			super("고객관리 프로그램");
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
			this.add(name); this.add(writer); this.add(namet);  this.add(writert);
			this.add(src); this.add(list); this.add(edit); this.add(del); this.add(close);
			this.add(info); this.add(inname); this.add(innamet); this.add(inwriter); this.add(inwritert); this.add(incord); this.add(incordt);
			this.add(publish); this.add(publisht); this.add(bookin); 
			
			
			name.setFont(font13); writer.setFont(font13); info.setFont(font13); list.setFont(font12);
			
			name.setBounds(80, 50, 60, 30); namet.setBounds(140, 50, 80, 30);
			writer.setBounds(280, 50, 80, 30); writert.setBounds(370, 50, 100, 30);
			src.setBounds(480, 50, 50, 30);
			list.setBounds(190, 100, 470, 280);
			 edit.setBounds(280, 400, 80, 30); del.setBounds(380, 400, 80, 30); 
			bookin.setBounds(180, 400, 80, 30); 
			close.setBounds(600, 400, 50, 30);
			info.setBounds(40, 100, 110, 30);
			inname.setBounds(30, 140, 50, 30); innamet.setBounds(90, 140, 90, 25);
			inwriter.setBounds(40, 180, 50, 30); inwritert.setBounds(90, 180, 90, 25);
			publish.setBounds(30, 220, 50, 30); publisht.setBounds(90, 220, 90, 25);
			incord.setBounds(20, 260, 60, 30); incordt.setBounds(90, 260, 90, 25);
			
	}
		void close() {this.setVisible(false);}
		void start() {
			src.addActionListener(this); bookin.addActionListener(this); edit.addActionListener(this); 
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
				String writer = writert.getText();
				if(name.equals("")) {msgDlg("도서명을 입력하세요"); return;}
				if(writer.equals("")) {msgDlg("저자명을 입력하세요"); return;}
				
				Search(name, writer);}
			
			else if(e.getSource()==bookin) {BookInsert bookinsert = new BookInsert(); close();}
			else if(e.getSource()==edit) {BookEdit edit = new BookEdit(); close();}
			else if(e.getSource()==del) {Bookdelet delet = new Bookdelet(); close();}
			else if(e.getSource()==close) {close();}
		}
		
		void DataLoad(){
			
			Statement stmt = null; // 접속상태
			ResultSet rs = null; // db 주머니같은
			String query = "select * from tb_javaproject1"; //쿼리목록
			try {
				stmt = dc.createStatement(); //접속한 상태값
				rs = stmt.executeQuery(query); //끌어와서 실행
				String data="";
				while (rs.next()) {
					data+=rs.getString("name")+"/"+rs.getString("writer")+"/"+rs.getString("publish")+"/"+rs.getString("cord")+rs.getString("cord")+"\n"+"\n";
				}
				list.setText(data);
				rs.close();
				stmt.close();
			} catch (SQLException ee) {
				System.err.println("error = " + ee.toString());
			}
			
		}

		void Search(String name, String writer){
			boolean check=false;
			String query = "select * from tb_javaproject1 where name = ? and writer = ?";
			
			try {
				PreparedStatement pstmt = dc.prepareStatement(query);
				pstmt.setString(1, name);
				pstmt.setString(2, writer);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					check=true;
					innamet.setText(rs.getString("name"));
					inwritert.setText(rs.getString("writer"));
					publisht.setText(rs.getString("publish"));
					incordt.setText(rs.getString("cord"));
				}
				rs.close();
				pstmt.close();
			} catch (SQLException ee) {
				System.err.println("login 처리 실패!!");
			}
			if(check==false) {msgDlg("존재하지 않는 도서명");}
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
