package bulleteinboard.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;

import bulleteinboard.bean.Comment;
import bulleteinboard.bean.User;
import bulleteinboard.service.CommentService;

@WebServlet(urlPatterns = { "/Comment" })
public class CommentServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException, ServletException {

		HttpSession session = request.getSession();

		List<String> comments = new ArrayList<String>();

		if (isValid(request, comments) == true) {

			User user = (User) session.getAttribute("loginUser");

			Comment comment = new Comment();
			comment.setText(request.getParameter("comment"));
			comment.setUserId(user.getId());
			comment.setMessageId(Integer.parseInt(request.getParameter("messageId")));


			new CommentService().register(comment);

			response.sendRedirect("./");
		} else {
			session.setAttribute("errorMessages", comments);
			response.sendRedirect("./");
		}
	}

	private boolean isValid(HttpServletRequest request, List<String> comments) {

		String comment = request.getParameter("comment");

		if (StringUtils.isEmpty(comment) == true) {
			comments.add("コメント内容を入力してください");
		}
		if (500 < comment.length()) {
			comments.add("500文字以下で入力してください");
		}
		if (comments.size() == 0) {
			return true;
		} else {
			return false;
		}
	}

}
