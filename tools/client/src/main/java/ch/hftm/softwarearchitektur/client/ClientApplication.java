package ch.hftm.softwarearchitektur.client;

import java.io.File;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import ch.hftm.softwarearchitektur.client.webclient.FaceFindClient;
import picocli.CommandLine;
import picocli.CommandLine.Parameters;

@SpringBootApplication
public class ClientApplication implements Runnable {

	private static ConfigurableApplicationContext context;

	@Parameters(index = "0", description = "The file to analyze.")
    private File file;

    @Parameters(index = "1", description = "The URL to call.")
    private String url;

    @Override
    public void run() {
		new FaceFindClient()
			.call(url, file)
			.blockOptional()
			.ifPresent(System.out::println);
    }

	public static void main(String[] args) {
		context = SpringApplication.run(ClientApplication.class, args);
        new CommandLine(new ClientApplication()).execute(args);
	}
}
