## 	shiro笔记一

### shiro登录流程

1. 加载指定数据源,创建SecurityManagerFactory用于验证用户身份信息 :

   ​    ```IniSecurityManagerFactory factory = new IniSecurityManagerFactory("classpath:shiro.ini")```

2. 获取SecurityManager并绑定到SecurityUitls     ```SecurityUtils.setSecuritManager(factory.getInstance())```

3. 获取Subject    

   ​	```Subject subject = SecurityUtils.getSubject()```

4. 根据用户信息创建UsernamePasswordToken 保存用户信息用于与数据源信息做对比以验证

   ​    ```UsernamePasswordToken userToken = new UsernamePassword("username","password")```

5. 验证登录     

   ​    ```subject.login(userToken)```

### shiro认证流程

1. 当调用Subject.login(token)进行登陆时,用户验证操作将会被丢给SecurityManager进行验证。注：验证前需要全局绑定SecurityManager

2. SecuriyManager拿到验证请求后会将数据转交给Authenticator进行身份验证

3. Authenticator则将携带的userToken及请求委托给相应的Authenticator进行多Realm策略化验证 如无返回数据或抛出异常则说明身份验证失败

   > Realm:域，即存放用户身份数据的数据源
   >
   > 1. 单realm配置，多realm配置
   >
   >    - .ini配置文件配置securityManager.realms=realm1,realm2...等
   >
   > 2. JDBC Realm
   >
   >    - .ini配置
   >
   >   ```
   >   jdbcRealm=org.apache.shiro.realm.jdbc.JdbcRealm
   >   dataSource=com.alibaba.druid.pool.DruidDataSource
   >   dataSource.driverClassName=com.mysql.jdbc.Driver
   >   dataSource.url=jdbc:mysql://localhost:3306/shiro
   >   dataSource.username=root
   >   dataSource.password=123456
   >   jdbcRealm.dataSource=$dataSource
   >   securityManager.realms=$jdbcRealm&nbsp;
   >   ```
   >
   > Authenticator:验证用户账号，验证成功会返回AuthenticationInfo验证信息，失败则会抛出相应的AuthentitcationException的异常实现
   >
   > 1. FirsSuccessfulStrategy:只要一个Realm验证成功即可，只返回第一个Realm的身份信息
   >
   > 2. AtLeastOneSuccessfulStrategy:只要一个Realm验证成功即可,会返回所有成功的Realm信息
   >
   > 3. AllSuccessfulStrategy:所有的Realm验证成功才算成功 
   >
   >    ```
   >    .ini配置文件配置Authenticator验证策略
   >    authenticator=org.apache.shiro.authc.pam.ModularRealmAuthenticator
   >    securityManager.authenticator=$authenticator
   >    myRealm1 = com.realm.MyRealm
   >    myRealm12 = com.realm.MyRealm2
   >    securityManager.realms=$myRealm1,$myRealm13
   >    ```
   >
   >    

### shiro授权

1. 授权:控制访问，及用户拥有哪些权限能够访问哪些资源

   1. 基于角色的控制访问(隐式角色)

      ```
      [users]
      #在.ini配置文件中配置用户所属角色role，一个用户可以拥有多个角色
      zhang=123,role1,role2
      wang=123,role1
      ```

      - java代码验证

      ```
      //验证该用户是否属于role1,role2
      subject().hasRole("role1");
      subject().hasAllRoles(Arrays.asList("role1", "role2"));
      //验证该用户是否属于role1，role2
      subject().checkRole("Role1");
      subject().checkRoles("Role1","Role2");
      ```

      - 注:hasRole()验证为通过会返回false，checkRole验证未通过则会抛未授权异常

   2. 基于资源的访问控制(显示角色)

      ```
      [users]
      #在.ini配置文件中配置用户-角色及角色所拥有的权限
      zhang = 123,role1,role2
      wang = 123,role1
      role1 = user:create,user:update
      role2 = user:delete
      ```

      - java代码验证

        ```
        #验证用户是否拥有create，update，delete权限,返回true or false
        subject.isPermitted("user:create");
        subject.isPermittedAll("user:update,user:delete");
        #验证用户是否拥有create,update,delete权限,如果没有则抛出未授权异常
        subbject.checkPermissions("user:delete","user:update");
        subject.checkPerimssion("user:create");
        ```

2. 授权流程

   1. subject.login(token)：把token和请求交接给SubjectManager

   2. SubjectManager会继续把请求委托给授权管理者Authorizer。注:当调用isPremitted()时,先会通过 PermissionResolver 把字符串转换成相应的 Permission 实例，封装用户权限后，再封装到SimpleAuthorizationInfo 用户登录信息返回

      - ```java
          SimpleAuthorizationInfo simpleAuthorizationInfo = new SimpleAuthorizationInfo();
                //添加用户
                simpleAuthorizationInfo.addRoles(Arrays.asList("role1","role2"));
          		//添加权限
          		//自定义Permission：BitPermission
                simpleAuthorizationInfo.addObjectPermission(new BitPermission("+user1+10"));
                //使用默认通配符Permission权限验证
          		simpleAuthorizationInfo.addObjectPermission(new WildcardPermission("user1:*")); simpleAuthorizationInfo.addStringPermissions(Arrays.asList("+user2+10","user2:*"));
                return simpleAuthorizationInfo;
        ```

   3. Authorize会调用相应的 Realm 获取授权信息SimpleAuthorizationInfo,如果授权信息拥有角色的话，则会调用RolePermissionReslover实现类方法，将角色权限追加到SimpleAuthorizationInfo

      - ```java
        public class MyRolePermissionResolver implements RolePermissionResolver {
            public Collection<Permission> resolvePermissionsInRole(String s) {
            //用户属于角色role1时，添加权限menu:*
                if("role1".equals(s)) {
                    return Arrays.asList((Permission)new WildcardPermission("menu:*"));
                }
                return null;
            }
        }
        ```

   4. 循环遍历SimpleAuthorizationInfo的权限信息。注：调用Permission.implies进行比较，返回true则说明授权通过

      - ModularRealmAuthorizer 进行多 Realm 匹配流程
        - 首先检查相应的 Realm 是否实现了实现了 Authorizer；
        - 如果实现了 Authorizer，那么接着调用其相应的 `isPermitted*/hasRole*` 接口进行匹配；
        - 如果有一个 Realm 匹配那么将返回 true，否则返回 false。
      - PermissionResolver:用于解析权限字符串到 Permission 实例
      -  RolePermissionResolver: 用于根据角色解析相应的权限集合。

3. java方式注入shiro配置

   - ```
     /**
          * JAVA 注入进行配置
          */
         public void Di_Config(){
     
             DefaultSecurityManager defaultSecurityManager = new DefaultSecurityManager();
     
             /**设置authenticator**/
             ModularRealmAuthenticator authenticator = new ModularRealmAuthenticator();
             //设置realm拦截策略
             authenticator.setAuthenticationStrategy(new AtLeastOneSuccessfulStrategy());
             //设置SecurityManager的authenticator验证对象
             defaultSecurityManager.setAuthenticator(authenticator);
     
             //设置用户角色
             ModularRealmAuthorizer modularRealmAuthorizer = new ModularRealmAuthorizer();
             modularRealmAuthorizer.setPermissionResolver(new WildcardPermissionResolver());
             defaultSecurityManager.setAuthorizer(modularRealmAuthorizer);
     
            /*设置realm*/
             DruidDataSource dataSource = new DruidDataSource();
             dataSource.setDriverClassName("com.mysql.jdbc.Driver");
             dataSource.setUrl("jdbc:mysql://localhost:3306/shiro");
             dataSource.setUsername("root");
             dataSource.setPassword("123456");
             JdbcRealm jdbcRealm = new JdbcRealm();
             jdbcRealm.setDataSource(dataSource);
             jdbcRealm.setPermissionsLookupEnabled(true);
             defaultSecurityManager.setRealms(Arrays.asList((Realm)jdbcRealm));
     
             /*全局绑定SeurityManager*/
             SecurityUtils.setSecurityManager(defaultSecurityManager);
             Subject subject = SecurityUtils.getSubject();
             UsernamePasswordToken userToken = new UsernamePasswordToken("zhang","123");
             subject.login(userToken);
             //断言成功登陆
             Assert.assertTrue(subject.isAuthenticated());
         }
     ```


### shiro编码加密