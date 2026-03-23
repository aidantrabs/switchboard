import { useState } from "react";
import { createRoute } from "@tanstack/react-router";
import { useQuery } from "@tanstack/react-query";
import { flagQueries } from "../../../../api/flags";
import { FlagTable } from "../../../../components/flags/FlagTable";
import { CreateFlagForm } from "../../../../components/flags/CreateFlagForm";
import { PageLayout } from "../../../../components/layout/PageLayout";
import { Button } from "../../../../components/ui/button";
import { Card, CardContent } from "../../../../components/ui/card";
import { projectsRoute } from "../../../projects";
import { Plus, Flag, X } from "lucide-react";

export const flagListRoute = createRoute({
  getParentRoute: () => projectsRoute,
  path: "$projectKey/flags",
  component: FlagListPage,
});

function FlagListPage() {
  const { projectKey } = flagListRoute.useParams();
  const { data: flags, isLoading } = useQuery(flagQueries.list(projectKey));
  const [showCreate, setShowCreate] = useState(false);

  return (
    <PageLayout
      title="Feature Flags"
      description={projectKey}
      actions={
        <Button size="sm" onClick={() => setShowCreate(!showCreate)}>
          {showCreate ? <X size={14} /> : <Plus size={14} />}
          {showCreate ? "Cancel" : "New Flag"}
        </Button>
      }
    >
      {showCreate && (
        <CreateFlagForm projectKey={projectKey} onClose={() => setShowCreate(false)} />
      )}

      {isLoading ? (
        <Card className="animate-pulse">
          <CardContent className="py-12">
            <div className="h-4 w-48 rounded bg-muted mx-auto" />
          </CardContent>
        </Card>
      ) : !flags?.length && !showCreate ? (
        <Card>
          <CardContent className="flex flex-col items-center justify-center py-12">
            <Flag size={40} className="text-muted-foreground mb-3" />
            <p className="text-sm text-muted-foreground">No flags in this project yet.</p>
            <Button size="sm" variant="outline" className="mt-4" onClick={() => setShowCreate(true)}>
              <Plus size={14} />
              Create your first flag
            </Button>
          </CardContent>
        </Card>
      ) : (
        <FlagTable data={flags ?? []} projectKey={projectKey} />
      )}
    </PageLayout>
  );
}
