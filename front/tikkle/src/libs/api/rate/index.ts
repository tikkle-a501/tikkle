import { RateGetResponses } from "@/types";
import { handleApiRequest } from "../client";

export const fetchRate = async () => {
  return handleApiRequest<RateGetResponses, "get">("/rate", "get");
};
