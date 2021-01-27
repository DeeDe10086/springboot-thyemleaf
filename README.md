# springboot-thyemleaf

## 解题思路

- Springboot + mybatis-plus组成后台服务
- thyemleaf作为前端
- 后台先实现分页功能，前端页面再控制分页的传入
- 原本计划加入自定义pageSize的，但是放上去以后不好看，就砍掉了

## 解题过程

- 首先引入需要的pom依赖

  ```xml
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-thymeleaf</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-web</artifactId>
  </dependency>
  <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-test</artifactId>
      <scope>test</scope>
  </dependency>
  <dependency>
      <groupId>com.baomidou</groupId>
      <artifactId>mybatis-plus-boot-starter</artifactId>
      <version>3.3.2</version>
  </dependency>
  <dependency>
      <groupId>mysql</groupId>
      <artifactId>mysql-connector-java</artifactId>
  </dependency>
  ```

- 然后完成从控制层到持久层，获取博客博客数据列表

  - controller

    ```java
    @Controller
    public class BlogController {
    
        private Integer page = 1;
        private Integer pageSize = 2;
    
        private final TArticleService tArticleService;
    
        public BlogController(@Qualifier("tArticleService") TArticleService tArticleService) {
            this.tArticleService = tArticleService;
        }
    
        @RequestMapping("blog")
        public ModelAndView toSuccess(Integer page, Integer pageSize){
            if(page==null){
                page = this.page;
            }
            if(pageSize==null){
                pageSize = this.pageSize;
            }
            ModelAndView modelAndView = new ModelAndView();
    
            /**
             * 获取页面数据
             */
            Page<TArticle> pages= new Page<>(page, pageSize);
            tArticleService.getArticles(pages);
            /**
             * 数据包装到ModelAndView
             */
            modelAndView.addObject("result",pages);
    
            modelAndView.setViewName("client/index");
            return modelAndView;
        }
    }
    ```

  - serviceImpl

    ```java
    @Service("tArticleService")
    public class TArticleServiceImpl extends ServiceImpl<TArticleMapper, TArticle> implements TArticleService {
    
        @Override
        public Page<TArticle> getArticles(Page<TArticle> page){
            /**
             * 直接引用mybatis-plus的分页方法
             */
            super.page(page);
            return page;
        }
    }
    ```

  - mapper

    ```java
    public interface TArticleMapper extends BaseMapper<TArticle> {
    
    }
    ```

  - 定义一个mybatis-plus的配置类，用于配置mapper的包扫描路径，注册分页插件到bean

    ```java
    @Configuration
    @MapperScan(basePackages = {
            "com.lagou.homework.thymeleaf.mapper"
    })
    public class MybatisPlusConfig {
        @Bean
        public PaginationInterceptor paginationInterceptor() {
            PaginationInterceptor paginationInterceptor = new PaginationInterceptor();
            // 设置请求的页面大于最大页后操作， true调回到首页，false 继续请求  默认false
            // paginationInterceptor.setOverflow(false);
            // 设置最大单页限制数量，默认 500 条，-1 不受限制
            // paginationInterceptor.setLimit(500);
            // 开启 count 的 join 优化,只针对部分 left join
            paginationInterceptor.setCountSqlParser(new JsqlParserCountOptimize(true));
            return paginationInterceptor;
        }
    }
    ```

- 最后在index.html页面进行数据绑定

  ```html
  <div class="am-u-md-8 am-u-sm-12">
      <!--文章遍历并分页展示 : 需要同学们手动完成，基本样式已经给出，请使用th标签及表达式完成页面展示 -->
      <div th:object="${result}">
          <div style="height: 610px;">
              <div th:each="art:*{records}">
                  <article class="am-g blog-entry-article">
  
                      <div class="am-u-lg-6 am-u-md-12 am-u-sm-12 blog-entry-text">
                          <!-- 文章分类 -->
                          <span class="blog-color" style="font-size: 15px;"><a>默认分类</a></span>
                          <span>&nbsp;&nbsp;&nbsp;</span>
                          <!-- 发布时间 -->
                          <span style="font-size: 15px;" th:text="'发布于 '+ ${art.created}"/>
                          <h2>
                              <!-- 文章标题 -->
                              <div><a style="color: #0f9ae0;font-size: 20px;" th:text="${art.title}"/>
                              </div>
                          </h2>
                          <!-- 文章内容-->
                          <div style="font-size: 16px;" th:text="${art.content}"/>
                      </div>
                  </article>
              </div>
          </div>
          <!--定义分页-->
          <!--自定义页面大小的话，把pageSize改成从页面获取就行了-->
          <div th:with="value = ((*{total}%2>0)? ((*{total} - *{total}%2)/2+1) : (*{total}/2))">
              <a th:if="(*{current}-1)>0" th:href="@{/blog(page=1,pageSize=2)}">首页</a>
  
              <a th:if="(*{current}-1)>0" th:href="@{/blog(page=*{current}-1,pageSize=2)}">上一页</a>
              <a th:if="(*{current}+1)<=${value}" th:href="@{/blog(page=*{current}+1,pageSize=2)}">下一页</a>
  
              <a th:if="(*{current}+1)<=${value}" th:href="@{/blog(page=${value},pageSize=2)}">尾页</a>
          </div>
      </div>
  </div>
  ```

  分页的时候，实现了：

  第一页不显示”首页“和”上一页“

  最后一页不显示”尾页“和”下一页“

## 验证

启动服务后，访问地址：http://localhost:8080/blog

