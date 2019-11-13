package cn.edu.ruc.adcourse;

import com.google.gson.reflect.TypeToken;
import cn.edu.ruc.adcourse.data.FileInfo;
import cn.edu.ruc.adcourse.data.FileStorageInfo;
import cn.edu.ruc.adcourse.util.GsonUtil;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * 拦截器获取信息之后，给jsp
 * Created by rainmaple on 2019/11/3.
 */
@WebFilter(filterName = "FileInfoFilter", urlPatterns = "/FileInfo.jsp")
public class FileInfoFilter implements Filter{
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        String[] datas = new CommunicateWithFileServerStrategyImpl("127.0.0.1", 2533).getFileInfo();
        List<FileInfo> fileInfos = GsonUtil.getInstance().fromJson(datas[0], new TypeToken<List<FileInfo>>(){}.getType());
        Map<String, FileStorageInfo> groupInfos = GsonUtil.getInstance().fromJson(datas[1], new TypeToken<Map<String, FileStorageInfo>>(){}.getType());
        servletRequest.setAttribute("fileInfos", fileInfos);
        servletRequest.setAttribute("groupInfos", groupInfos);
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {

    }
}
