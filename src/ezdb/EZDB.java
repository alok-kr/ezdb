package ezdb;

import java.lang.annotation.Retention;
import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
public @interface EZDB {
	public String colDef() default "";
	public boolean autoIncrement() default false;
	public boolean isPrimary() default false;
}
