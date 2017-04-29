import org.junit.Test;

import javax.script.*;
import java.io.FileReader;
import java.net.URL;

/**
 * Created by Administrator on 2017/4/28/0028.
 */
public class JavascriptTest {
    @Test
    public void testScriptInterface() throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("javascript");
        ClassLoader classLoader = JavascriptTest.class.getClassLoader();

        URL resource = classLoader.getResource("Bmob/bmob.js");
        String path = resource.getPath();
        FileReader reader = new FileReader(path);   // 执行指定脚本
        engine.eval(reader);
//        String script = "<script language=javascript src='/D:/work/Idea/spider/target/test-classes/Bmob/bmob-min.js'></script>";
        String script = "Bmob.initialize(\"fe2d0963f4f6019ccb3a23a76de05397\", \"b2584e80f16b216bef83fe4640463354\");";
        engine.eval(script);

        resource = classLoader.getResource("Bmob/MyBmob.js");
        path = resource.getPath();
        System.out.println(path);
        reader = new FileReader(path);   // 执行指定脚本
        engine.eval(reader);
        if (engine instanceof Invocable) {
            Invocable invoke = (Invocable) engine;    // 调用merge方法，并传入两个参数
            Double c = (Double) invoke.invokeFunction("merge", 2, 3);
            System.out.println("c = " + c);
            invoke.invokeFunction("init", 1);
        }
        reader.close();
    }


    @Test
    public void Script() {
        try {
            ScriptEngine engine = new ScriptEngineManager()
                    .getEngineByName("javascript");
            Compilable compilable = (Compilable) engine;
            Bindings bindings = engine.createBindings(); // Local级别的Binding
            String script = "function add(op1,op2){return op1+op2} add(a, b)"; //
            // 定义函数并调用
            CompiledScript JSFunction = compilable.compile(script); // 解析编译脚本函数
            bindings.put("a", 1);
            bindings.put("b", 2); // 通过Bindings加入参数
            Object result = JSFunction.eval(bindings);
            System.out.println(result); // 调用缓存着的脚本函数对象，Bindings作为参数容器传入
        } catch (ScriptException e) {
            System.out.println(e.getMessage());
        }
    }

}
