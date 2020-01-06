package com.chargepile.util;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

@SuppressWarnings("serial")
public class CommonPaged implements Serializable{
	private int total ; //总记录数
	private List<Map<String,Object>> list = new java.util.ArrayList<Map<String,Object>>();
	private int currentPage; //当前页数
	private int pageSize ;   //每页的条数
	private int totalPage ;   //总的页面数
	
	public int getTotalPage() {
		return totalPage;
	}
	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public CommonPaged(){
		
	}
	public CommonPaged(int total,int currentPage,int pageSize,List list){
		this.total = total;
		this.list  = list;
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		
		this.totalPage = total / pageSize;
        if (totalPage * pageSize < total)  //如果还有剩余，那么页数加1
            totalPage += 1;
	}
	public int getTotal() {
		return total;
	}
	public void setTotal(int total) {
		this.total = total;
	}
	
	
	public List<Map<String, Object>> getList() {
		return list;
	}
	public void setList(List<Map<String, Object>> list) {
		this.list = list;
	}
	public void setTotalPage(int totalPage) {
		this.totalPage = totalPage;
	}
	/**
	 * 根据总记录，当前页、页数自动产生分页的HTML
	 * @param total  总的记录数
	 * @param currentPage 当前页
	 * @param pageSize  每页的条数
	 * @param url  链接的URL
	 * @return
	 */
	public static String generatePageHtml(int total,int currentPage,int pageSize,String url){
		CommonPaged cp = new CommonPaged(total,currentPage,pageSize,null);
		return cp.generatePageLinkStyle(url);
	}
	public static String generatePageHtml(int total,int currentPage,String url){
		return generatePageHtml(total,currentPage,20,url);
	}
	public static String generatePageHtml(int total,HttpServletRequest request,String url){
		int currentPage = getPageNumber(request);
		return generatePageHtml(total,currentPage,20,url);
	}
	public static String generatePageHtml(int total,HttpServletRequest request,int pageSize,String url){
		int currentPage = getPageNumber(request);
		return generatePageHtml(total,currentPage,pageSize,url);
	}
	public static String generatePageHtml1(int total,int currentPage,int pageSize,String url){
		CommonPaged cp = new CommonPaged(total,currentPage,pageSize,null);
		return cp.generatePageLinkStyle2(url);
	}
	
	private String buildOneLink(int onePage,String url){
		if(currentPage==onePage) //如果是当前页
			return "<span class='current'>"+onePage+"</span>";
		else
			return "<a href="+url+"pageNo="+onePage+">"+onePage+"</a>";  
	}
	
	private String buildPrevLink(String url){
		if(currentPage==1){
			return "<span class=disabled>&lt;上一页</span>";
		}else{
			return "<a href="+url+"pageNo="+(currentPage-1)+">&lt;上一页</a>";  
		}
	}
	private String buildNextLink(String url){
		if(totalPage==currentPage){
			return "<span class=disabled>下一页&gt;</span>";
		}else{
			return "<a href="+url+"pageNo="+(currentPage+1)+">下一页&gt;</a>";  
		}
	}


    private String buildOneLink3(int onePage,String url,String pageFlag){
		if(currentPage==onePage) //如果是当前页
			return "<span class='current'>"+onePage+"</span>";
		else
			return "<a href="+url+pageFlag+"="+onePage+">"+onePage+"</a>";
	}


    private String buildPrevLink3(String url,String pageFlag){
		if(currentPage==1){
			return "<span class=disabled>&lt;上一页</span>";
		}else{
			return "<a href="+url+pageFlag+"="+(currentPage-1)+">&lt;上一页</a>";
		}
	}

    private String buildNextLink3(String url,String pageFlag){
		if(totalPage==currentPage){
			return "<span class=disabled>下一页&gt;</span>";
		}else{
			return "<a href="+url+pageFlag+"="+(currentPage+1)+">下一页&gt;</a>";
		}
	}


    public String   generatePageLinkStyle3(String url ,String pageFlag){
        StringBuffer buf = new StringBuffer();
		if(totalPage>1){ //大于1才分页
			url = buildUrl(url);
			//生成上一页
			buf.append(buildPrevLink3(url,pageFlag));
			if(totalPage<=10){ //如果小于10页，那么直接打印出来就可以
				for(int i=1;i<=totalPage;i++){
					buf.append(buildOneLink3(i,url,pageFlag));
				}
			}else if(totalPage>10){
				if(currentPage<=5){//如果当前页小于5，那么全部显示前10条
					for(int i=1;i<=10;i++){  //生成10之前的代码
						buf.append(buildOneLink3(i,url,pageFlag));
					}
					if(totalPage<=12){ //如果总页数小于12，那么显示所有
						for(int i=11;i<=totalPage;i++){
							buf.append(buildOneLink3(i,url,pageFlag));
						}
					}else{//如果总页数大于12，那么显示...最后两个
						buf.append("...");
						for(int i=totalPage-1;i<=totalPage;i++){
							buf.append(buildOneLink3(i,url,pageFlag));
						}
					}
				}else{ //如果当前页大于5,那么显示1,2,...
					for(int i=1;i<=2;i++){
						buf.append(buildOneLink3(i,url,pageFlag));
					}
					buf.append("...");
					if(totalPage<=currentPage+9){//本身是7个，然后后面2个如果存在也应该显示，因此是9个
						if(totalPage-currentPage<7){//如果接近末位
							for(int i=totalPage-10;i<=totalPage;i++){//那么取后面10个
								buf.append(buildOneLink3(i,url,pageFlag));
							}
						}else{
							for(int i=currentPage-2;i<=totalPage;i++){//否则还是取前面2个，再加后面所有的
								buf.append(buildOneLink3(i,url,pageFlag));
							}
						}
					}else{
						for(int i=currentPage-2;i<=currentPage+7;i++){
							buf.append(buildOneLink3(i,url,pageFlag));
						}
						buf.append("...");
						for(int i=totalPage-1;i<=totalPage;i++){
							buf.append(buildOneLink3(i,url,pageFlag));
						}
					}
				}
			}
			buf.append(buildNextLink3(url,pageFlag));
			buf.append("共"+totalPage+"页");
		}
       return buf.toString();
    }

    public String generatePageLinkStyle(String url,String preImage,String nextImage){
    	StringBuffer sb=new StringBuffer();
    	if(currentPage==1){
    		sb.append("<span class=disabled><img src='"+preImage+"'></img></span>");
		}else{
			sb.append("<a href="+url+"&pageNo="+(currentPage-1)+"><img src='"+preImage+"'></img></a>");
		}
    	
    	if(totalPage==currentPage){
    		sb.append("<span class=disabled><img src='"+nextImage+"'></img></span>");
		}else{
			sb.append("<a href="+url+"&pageNo="+(currentPage+1)+"><img src='"+nextImage+"'></img></a>");
		}
    	return sb.toString();
    }

	/**
	 * 原则是：前面2个，本身1个，后面8个，然后最前面2个，加上...，最后面2个，加上...
	 * @param url
	 * @return
	 */
	public String generatePageLinkStyle(String url){
		StringBuffer buf = new StringBuffer();
		if(totalPage>1){ //大于1才分页
			url = buildUrl(url);
			//生成上一页
			buf.append(buildPrevLink(url));
			if(totalPage<=12){ //如果小于12页，那么直接打印出来就可以
				for(int i=1;i<=totalPage;i++){
					buf.append(buildOneLink(i,url));
				}
			}else if(totalPage>12){
				if(currentPage<=5){//如果当前页小于5，那么全部显示前10条
					for(int i=1;i<=10;i++){  //生成10之前的代码
						buf.append(buildOneLink(i,url));
					}
					buf.append("...");
					for(int i=totalPage-1;i<=totalPage;i++){
						buf.append(buildOneLink(i,url));
					}
				}else{ //如果当前页大于5,那么显示1,2,...
					for(int i=1;i<=2;i++){
						buf.append(buildOneLink(i,url));
					}
					buf.append("...");
					if(totalPage<=currentPage+9){//本身是7个，然后后面2个如果存在也应该显示，因此是9个
						if(totalPage-currentPage<7){//如果接近末位
							for(int i=totalPage-10;i<=totalPage;i++){//那么取后面10个
								buf.append(buildOneLink(i,url));
							}
						}else{
							for(int i=currentPage-2;i<=totalPage;i++){//否则还是取前面2个，再加后面所有的
								buf.append(buildOneLink(i,url));
							}
						}
					}else{
						for(int i=currentPage-2;i<=currentPage+7;i++){
							buf.append(buildOneLink(i,url));
						}
						buf.append("...");
						for(int i=totalPage-1;i<=totalPage;i++){
							buf.append(buildOneLink(i,url));
						}
					}
				}
			}
			buf.append(buildNextLink(url));
			buf.append("共"+totalPage+"页");
		}
       return buf.toString();     
	}
	private String buildUrl(String url){
		if(url.indexOf("?")>0)
			return url+"&";
		else 
			return url+"?";
	}
	
	public String generatePageLinkStyle2(String url){
		 StringBuffer buf=new StringBuffer();
		 buf.append("共有"+total+"条记录，分"+totalPage+"显示,当前"+currentPage+"页");
		 url=this.buildUrl(url);
         if(currentPage != 1){ //前页
             buf.append("<a href="+url+"pageNo=1"+">第一页&nbsp;</a>\n");
         }else{
             buf.append("<span class='disabled'>第一页&nbsp;</span>\n");
         }
         if(currentPage > 1){ //上一页
             buf.append("<a href="+url+"pageNo="+(currentPage-1)+">上一页&nbsp;</a>\n");
         }else{
        	 buf.append("<span class='disabled'>上一页&nbsp;</span>\n");
         }
         if(currentPage < totalPage){ //下一页
             buf.append("<a href="+url+"pageNo="+(currentPage+1)+">下一页&nbsp;</a>\n");
         }else{
        	 buf.append("<span class='disabled'>下一页&nbsp;</span>\n");
         }
         if(currentPage != totalPage && totalPage != 0){ //后页
             buf.append("<a  href="+url+"pageNo="+totalPage+">后页</a>");
         }else{
        	 buf.append("<span class='disabled'>最后一页&nbsp;</span>\n");
         }
         return buf.toString();
	}
	public static int getPageNumber(HttpServletRequest request){
		 int pageNo = 1;
		 if(request.getParameter("pageNo")!=null){
	           try{
	        	 pageNo=Integer.valueOf(request.getParameter("pageNo"));
	           }catch(Exception e){

	           }
	     }
		 return pageNo;
	}
	
}
