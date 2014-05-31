package org.imartynov.lab;

import java.util.List;

import org.eclipse.aether.RepositorySystem;
import org.eclipse.aether.RepositorySystemSession;
import org.eclipse.aether.artifact.Artifact;
import org.eclipse.aether.artifact.DefaultArtifact;
import org.eclipse.aether.resolution.ArtifactRequest;
import org.eclipse.aether.resolution.ArtifactResult;
import org.eclipse.aether.resolution.VersionRangeRequest;
import org.eclipse.aether.resolution.VersionRangeResult;
import org.eclipse.aether.version.Version;

/**
 * Hello world!
 *
 */
public class App {
	static void resolveVersionRange() throws Exception {
		RepositorySystem system = Booter.newRepositorySystem();
		RepositorySystemSession session = Booter.newRepositorySystemSession(system);

		Artifact artifact = new DefaultArtifact("org.eclipse.aether:aether-util:[0,)");

		VersionRangeRequest rangeRequest = new VersionRangeRequest();
		rangeRequest.setArtifact(artifact);
		rangeRequest.setRepositories(Booter.newRepositories(system, session));

		VersionRangeResult rangeResult = system.resolveVersionRange(session, rangeRequest);
		List<Version> versions = rangeResult.getVersions();

		System.out.println("Available versions " + versions);
	}

	static void resolveArtifact() throws Exception {
		System.out.println("------------------------------------------------------------");
		RepositorySystem system = Booter.newRepositorySystem();
		RepositorySystemSession session = Booter.newRepositorySystemSession(system);

		Artifact artifact = new DefaultArtifact("org.eclipse.aether:aether-util:1.0.0.v20140518");

		ArtifactRequest artifactRequest = new ArtifactRequest();
		artifactRequest.setArtifact(artifact);
		artifactRequest.setRepositories(Booter.newRepositories(system, session));

		ArtifactResult artifactResult = system.resolveArtifact(session, artifactRequest);
		artifact = artifactResult.getArtifact();
		System.out.println(artifact + " resolved to  " + artifact.getFile());
	}

	public static void main(String[] args) {
		System.out.println("Hello World!");
	}
}
